package adf.database;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import com.proto.transporter.ObjectList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisTransporter {
  final Jedis jedis;
  private final AtomicReference<CompletableFuture<ObjectList>> responseRef = new AtomicReference<>();
  private final Thread subscribeThread;
  private final JedisPubSub jedisPubSub = new JedisPubSub() {
    @Override
    public void onMessage(String channel, String message) {
      try {
        ObjectList response = ObjectList.parseFrom(Base64.getDecoder().decode(message));
        CompletableFuture<ObjectList> responseFuture = responseRef.getAndSet(null);
        if (responseFuture != null) {
          responseFuture.complete(response);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };

  public RedisTransporter(String host, int port) {
    jedis = new Jedis(host, port);
    subscribeThread = new Thread(this::subscribe);
    subscribeThread.start();
  }

  private void subscribe() {
    Jedis jedis = new Jedis("localhost", 6379);
    try {
      jedis.subscribe(jedisPubSub, "response");
    } finally {
      jedis.close();
    }
  }

  public ObjectList getEcho(ObjectList request) {
    jedis.publish("request", Base64.getEncoder().encodeToString(request.toByteArray()));
    CompletableFuture<ObjectList> responseFuture = new CompletableFuture<>();
    responseRef.set(responseFuture);
    ObjectList response = responseFuture.join();
    return response;
  }

  public void close() {
    jedisPubSub.unsubscribe();
    jedis.close();
    subscribeThread.interrupt();
  }
}

package adf.database;

import java.util.Base64;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import com.proto.transporter.ObjectList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class RedisTranspoter {
  final Jedis jedis;

  public RedisTranspoter(String host, int port) {
    jedis = new Jedis(host, port);
  }

  public ObjectList getEcho(ObjectList request) {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<ObjectList> responseRef = new AtomicReference<>();

    // Create a new thread for subscribing to the response
    new Thread(() -> {
      try (Jedis jedisSubscribe = new Jedis("localhost", 6379)) {
        jedisSubscribe.subscribe(new JedisPubSub() {
          @Override
          public void onMessage(String channel, String message) {
            try {
              ObjectList response = ObjectList.parseFrom(Base64.getDecoder().decode(message));
              responseRef.set(response); // Set the received data
              latch.countDown(); // Decrease the latch count
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }, "response");
      }
    }).start();

    // Publish the message to the channel byte channel
    jedis.publish("request", Base64.getEncoder().encodeToString(request.toByteArray()));

    try {
      latch.await(); // Wait until the latch count reaches zero
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      System.err.println("Interrupted while waiting for response");
    }

    return responseRef.get(); // Return the received data
  }

  public void close() {
    jedis.close();
  }
}
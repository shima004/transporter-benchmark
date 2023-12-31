/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package adf.database;

import java.util.stream.IntStream;

import com.proto.transporter.Object;
import com.proto.transporter.ObjectList;

public class App {
    public static void main(String[] args) {
        RedisTransporter redisTranspoter = new RedisTransporter("localhost", 6379);
        ObjectList request = ObjectList.newBuilder()
                .addAllObjects(IntStream.range(0, 100).boxed()
                        .map(i -> Object.newBuilder().setX(Math.random()).build())
                        .toList())
                .setUuid("1234567890")
                .build();
        for (int i = 0; i < 100; i++) {
            ObjectList response = redisTranspoter.getEcho(request);
            // System.out.println("AAA Received message: " + response.toString());
            System.out.println(i);
        }
        redisTranspoter.close();
    }
}

package adf.socket;

import java.util.stream.IntStream;

import com.proto.transporter.Object;
import com.proto.transporter.ObjectList;

import adf.socket.transpoter.BaseSocketTranspoter;
import adf.socket.transpoter.InetSocketTranspoter;
import adf.socket.transpoter.UnixDomainSocketTranspoter;

public class App {
    public static void main(String[] args) {
        BaseSocketTranspoter socketTranspoter = null;
        int time = args.length > 0 ? Integer.parseInt(args[0]) : 30000;
        int count = args.length > 1 ? Integer.parseInt(args[1]) : 30000;
        String type = args.length > 2 ? args[2] : "Inet";
        if (type.equals("Inet")) {
            try {
                socketTranspoter = new InetSocketTranspoter("localhost", 8080);
            } catch (Exception e) {
                System.err.println(e);
            }
        } else if (type.equals("Unix")) {
            try {
                socketTranspoter = new UnixDomainSocketTranspoter("/tmp/socket.sock");
            } catch (Exception e) {
                System.err.println(e);
            }
        } else {
            System.err.println("Invalid type: " + type);
            return;
        }

        ObjectList request = ObjectList.newBuilder()
                .addAllObjects(IntStream.range(0, 100).boxed()
                        .map(i -> Object.newBuilder().setX(Math.random()).build())
                        .toList())
                .build();

        long interval_ns = time * (1000000 / count);
        System.out.println("Time: " + time + " ms" + " Count: " + count + " Interval: " + interval_ns + " ns");
        for (int i = 0; i < count; i++) {
            long start = System.nanoTime();
            try {
                socketTranspoter.getEcho(request);
            } catch (Exception e) {
                System.err.println(e);
            }
            long end = System.nanoTime();
            // Calculate the time taken for the request and response
            long timeTaken = end - start;
            // Sleep for the calculated interval before sending the next request
            if (i % 1000 == 0) {
                System.out.println(
                        "Time taken: " + timeTaken + " ns" + " Interval: " + interval_ns + " ns" + " Count: " + i);
            }
            try {
                if (timeTaken < interval_ns)
                    Thread.sleep((interval_ns - timeTaken) / 1000000, (int) ((interval_ns - timeTaken) % 1000000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

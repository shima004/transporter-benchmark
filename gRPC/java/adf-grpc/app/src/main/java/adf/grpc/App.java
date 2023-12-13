package adf.grpc;

import java.util.List;

import com.proto.transporter.Object;
import com.proto.transporter.ObjectList;

public class App {
  public static void main(String[] args) {
    GRPCTranspoter app = new GRPCTranspoter();
    List<Object> requestList = app.createRequestList(100).stream().map(x -> {
      return Object.newBuilder().setX(x).build();
    }).toList();
    ObjectList request = ObjectList.newBuilder().addAllObjects(requestList).build();
    int time = args.length > 0 ? Integer.parseInt(args[0]) : 30000;
    int count = args.length > 1 ? Integer.parseInt(args[1]) : 30000;
    // Calculate the interval between requests in milliseconds
    int interval = time / count;
    for (int i = 0; i < count; i++) {
      long start = System.currentTimeMillis();
      app.getEcho(request);
      long end = System.currentTimeMillis();
      // Calculate the time taken for the request and response
      long timeTaken = end - start;
      // Sleep for the calculated interval before sending the next request
      try {
        if (interval > timeTaken) {
          Thread.sleep(interval - timeTaken);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

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
    ObjectList response = app.getEcho(request);
    System.out.println(response);
  }
}

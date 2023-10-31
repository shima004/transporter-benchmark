package adf.socket.transpoter;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import com.proto.transporter.ObjectList;

public class InetSocketTranspoter extends BaseSocketTranspoter {
  Socket socket = null;

  public InetSocketTranspoter(String host, int port) throws Exception {
    try {
      socket = new Socket(host, port);
    } catch (Exception e) {
      throw e;
    }
    if (socket == null) {
      throw new Exception("Socket is null");
    }
  }

  public ObjectList getSortedObjectList(ObjectList request) throws Exception {
    try {
      // create output stream
      OutputStream outputStream = socket.getOutputStream();

      // send request
      outputStream.write(request.toByteArray());

      // create input stream
      InputStream inputStream = socket.getInputStream();

      // receive response
      byte[] recvBytes = new byte[8192];
      int recvSize = inputStream.read(recvBytes);
      byte[] message_recv_bytes = Arrays.copyOfRange(recvBytes, 0, recvSize);
      // parse response
      ObjectList message_recv = ObjectList.parseFrom(message_recv_bytes);
      return message_recv;
    } catch (Exception e) {
      throw e;
    }
  }
}

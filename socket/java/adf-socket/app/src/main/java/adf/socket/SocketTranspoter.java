package adf.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import com.proto.transporter.ObjectList;

public class SocketTranspoter {
  Socket socket = null;

  public SocketTranspoter(String host, int port) throws Exception {
    try {
      socket = new Socket(host, port);
    } catch (Exception e) {
      throw e;
    }
    if (socket == null) {
      throw new Exception("Socket is null");
    }
  }

  public void close() throws Exception {
    try {
      socket.close();
    } catch (Exception e) {
      throw e;
    }
  }

  public ObjectList getSortedObjectList(ObjectList request) throws Exception {
    try {
      // オブジェクトを送信するためのストリームを作成
      OutputStream outputStream = socket.getOutputStream();

      // オブジェクトを送信
      outputStream.write(request.toByteArray());

      // オブジェクトを受信するためのストリームを作成
      InputStream inputStream = socket.getInputStream();

      // オブジェクトを受信
      byte[] recvBytes = new byte[8192];
      int recvSize = inputStream.read(recvBytes);
      byte[] message_recv_bytes = Arrays.copyOfRange(recvBytes, 0, recvSize);
      // デシリアル化する
      ObjectList message_recv = ObjectList.parseFrom(message_recv_bytes);
      return message_recv;
    } catch (Exception e) {
      throw e;
    }
  }
}

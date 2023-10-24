package adf.socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnixDomainSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.proto.transporter.ObjectList;

public class UnixDomainSocketTranspoter extends BaseSocketTranspoter {
  Socket socket = null;

  public UnixDomainSocketTranspoter(String path) throws Exception {
    try {
      Path unixDomainSocketPath = Paths.get(path);
      socket = new Socket();
      socket.connect(UnixDomainSocketAddress.of(unixDomainSocketPath));
    } catch (Exception e) {
      throw e;
    }
    if (socket == null) {
      throw new Exception("Socket is null");
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
      ObjectList response = ObjectList.parseFrom(message_recv_bytes);
      return response;
    } catch (Exception e) {
      throw e;
    } finally {
      // socketをクローズする
      if (socket != null) {
        socket.close();
      }
    }
  }
}
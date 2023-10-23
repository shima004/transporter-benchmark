package adf.socket;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

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

  public ObjectList getSortedObjectList(ObjectList request) throws Exception {
    try {
      // オブジェクトを送信するためのストリームを作成
      OutputStream outputStream = socket.getOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

      // オブジェクトを送信
      objectOutputStream.writeObject(request);

      // オブジェクトを受信するためのストリームを作成
      InputStream inputStream = socket.getInputStream();
      ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

      // オブジェクトを受信
      Object object = objectInputStream.readObject();

      // 受信したオブジェクトがObjectListであることを確認し、キャストして返す
      if (object instanceof ObjectList) {
        return (ObjectList) object;
      } else {
        throw new Exception("Received object is not ObjectList");
      }
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

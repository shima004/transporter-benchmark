package adf.socket;

import java.io.IOException;
import java.net.Socket;

import com.proto.transporter.ObjectList;

public abstract class BaseSocketTranspoter {
  protected Socket socket = null;

  public void close() throws IOException {
    try {
      socket.close();
    } catch (IOException e) {
      throw e;
    }
  }

  public abstract ObjectList getSortedObjectList(ObjectList request) throws Exception;
}

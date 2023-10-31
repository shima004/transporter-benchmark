package adf.socket.transpoter;

import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import com.proto.transporter.ObjectList;

public class UnixDomainSocketTranspoter extends BaseSocketTranspoter {
  SocketChannel socketChannel = null;

  public UnixDomainSocketTranspoter(String path) throws Exception {
    try {
      socketChannel = SocketChannel.open(StandardProtocolFamily.UNIX);
      socketChannel.connect(UnixDomainSocketAddress.of(path));
      socketChannel.configureBlocking(true);
    } catch (Exception e) {
      throw e;
    }
    if (socketChannel == null) {
      throw new Exception("Socket is null");
    }
  }

  @Override
  public void close() throws IOException {
    try {
      socketChannel.close();
    } catch (IOException e) {
      throw e;
    }
  }

  public ObjectList getSortedObjectList(ObjectList request) throws Exception {
    try {
      socketChannel.write(ByteBuffer.wrap(request.toByteArray()));
      ByteBuffer buffer = ByteBuffer.allocate(8192);
      socketChannel.read(buffer);
      buffer.flip();
      byte[] message_recv_bytes = new byte[buffer.remaining()];
      buffer.get(message_recv_bytes);
      ObjectList response = ObjectList.parseFrom(message_recv_bytes);
      return response;
    } catch (Exception e) {
      throw e;
    }
  }
}
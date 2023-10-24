package adf.socket;

import java.util.List;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import com.proto.transporter.Object;
import com.proto.transporter.ObjectList;

@State(Scope.Benchmark)
public class BenchmarkTest {
  private SocketTranspoter socketTranspoter;
  private ObjectList testObjectList;

  @Setup
  public void setup() {
    try {
      socketTranspoter = new SocketTranspoter("localhost", 8080);
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<Object> requestList = IntStream.range(0, 100).mapToObj(i -> {
      return Object.newBuilder().setX(Math.random()).build();
    }).toList();
    testObjectList = ObjectList.newBuilder().addAllObjects(requestList).build();
  }

  @Benchmark
  public void testBenchmark() {
    try {
      socketTranspoter.getSortedObjectList(testObjectList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @TearDown
  public void tearDown() {
    try {
      socketTranspoter.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

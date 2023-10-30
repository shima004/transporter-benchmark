package adf.socket;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import com.proto.transporter.Object;
import com.proto.transporter.ObjectList;

import adf.socket.transpoter.InetSocketTranspoter;

@State(Scope.Benchmark)
public class InetSocketBenchmarkTest {
  private InetSocketTranspoter socketTranspoter;
  private ObjectList testObjectList;

  @Setup
  public void setup() {
    try {
      socketTranspoter = new InetSocketTranspoter("localhost", 8080);
    } catch (Exception e) {
      e.printStackTrace();
    }
    List<Object> requestList = IntStream.range(0, 100).mapToObj(i -> {
      return Object.newBuilder().setX(Math.random()).build();
    }).toList();
    testObjectList = ObjectList.newBuilder().addAllObjects(requestList).build();
  }

  @Benchmark
  @BenchmarkMode({ Mode.AverageTime })
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void benchmarkInetSocket() {
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

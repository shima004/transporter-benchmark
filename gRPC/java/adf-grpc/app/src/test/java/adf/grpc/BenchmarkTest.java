package adf.grpc;

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

@State(Scope.Benchmark)
public class BenchmarkTest {
  private GRPCTranspoter grpcTranspoter;
  private ObjectList testObjectList;

  @Setup
  public void setup() {
    grpcTranspoter = new GRPCTranspoter();
    List<Object> requestList = IntStream.range(0, 100).mapToObj(i -> {
      return Object.newBuilder().setX(Math.random()).build();
    }).toList();
    testObjectList = ObjectList.newBuilder().addAllObjects(requestList).build();
  }

  @TearDown
  public void teardown() {
    grpcTranspoter.channel.shutdown();
  }

  @Benchmark
  @BenchmarkMode({ Mode.AverageTime })
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void benchmarkGRPC() {
    grpcTranspoter.getEcho(testObjectList);
  }
}

package adf.grpc;

import java.util.List;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

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

  @Benchmark
  public void testBenchmark() {
    grpcTranspoter.getSortedObjects(testObjectList);
  }
}

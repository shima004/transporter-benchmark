package adf.database;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import com.proto.transporter.Object;
import com.proto.transporter.ObjectList;

@State(Scope.Benchmark)
public class RedisTransporterBenchmark {
  private RedisTransporter redisTransporter;
  private ObjectList testObjectList;

  @Setup
  public void setup() {
    redisTransporter = new RedisTransporter("localhost", 6379);
    List<Object> requestList = IntStream.range(0, 100).mapToObj(i -> {
      return Object.newBuilder().setX(Math.random()).build();
    }).toList();
    testObjectList = ObjectList.newBuilder().addAllObjects(requestList).build();
  }

  @TearDown
  public void teardown() {
    redisTransporter.close();
  }

  @Benchmark
  @BenchmarkMode({ org.openjdk.jmh.annotations.Mode.AverageTime })
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  public void averageTimeBenchmark() {
    redisTransporter.getEcho(testObjectList);
  }
}

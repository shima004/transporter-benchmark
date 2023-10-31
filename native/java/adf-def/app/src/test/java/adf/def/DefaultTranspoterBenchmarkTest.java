package adf.def;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class DefaultTranspoterBenchmarkTest {
  private DefaultTransport transport = new DefaultTransport();
  private List<Double> input = IntStream.range(0, 100).mapToDouble(i -> Math.random()).boxed().toList();

  @Benchmark
  @BenchmarkMode({ Mode.AverageTime })
  @OutputTimeUnit(TimeUnit.NANOSECONDS)
  public void benchmarkDefaultTransport() {
    transport.getEcho(input);
  }
}

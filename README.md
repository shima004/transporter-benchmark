# Transpoter Benchmark

## Environment

### java

- java version 17.0.2-openjdk (manage by sdkman)
- gradle version 7.2 (manage by sdkman)

### python

- python version 3.12.0 (manage by pyenv)
- poetry version 1.6.1
  - black version 23.10.0 (manage by poetry)
  - isort version 5.12.0 (manage by poetry)
  - flake8 version 6.1.0 (manage by poetry)

## Implementation

長さ 100 の double 配列を java から python に送信し、python で受け取った配列をソートして java に返す。

## Benchmark(benchmark by jmh)

### gRPC(wsl2)

```
Benchmark                     Mode  Cnt     Score     Error  Units
BenchmarkTest.testBenchmark  thrpt    5  2043.647 ± 256.875  ops/s
```

### socket(

```
Benchmark                     Mode  Cnt     Score      Error  Units
BenchmarkTest.testBenchmark  thrpt    5  6648.435 ± 1165.103  ops/s
```

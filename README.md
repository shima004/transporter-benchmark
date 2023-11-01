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

長さ 100 の double 配列を java から python に送信し、そのまま返す。

## Benchmark(benchmark by jmh)

### Run Benchmark

```bash
docker build -t benchmark.
docker run --rm -v results:/app/results -it benchmark
```

### Benchmark Result

[Benchmark Result](./results/)

# stream-fusion-bench

[![Build Status](https://travis-ci.org/strymonas/java8-benchmarks.svg?branch=master)](https://travis-ci.org/strymonas/java8-benchmarks)

This project is used as a benchmarking project for sequential streams of popular libraries that run on JVM. Our goal is to experiment and understand their fusion properties.

## Benchmark List

- sum: fusion of the collection and traversal
- sum of squares: additionally a mapping
- sum of squares even: additionally a filtering
- cartesian product: nested loops
- multiple maps: vertical fusion + megamorphic calls (function object)
- multiple filters: vertical fusion + megamorphic calls (function object)
- flatMap followed by take: short circuiting
- zipping
   - dotProduct: the simplest case
   - flatMap after zipWith: nested loop and zipping
   - zipWith after flatMap: similar
   - zipWith with two filters: changing the rate of production
   - zipWith with two flatMaps: parallel loops

## How to run

```shell
$ cd java8-benchmarks
$ mvn clean install
$ java -Xms6g -Xmx6g -XX:-TieredCompilation -jar target/benchmarks.jar -i 10 -wi 10 -f1 .* # to run all benchmarks
```

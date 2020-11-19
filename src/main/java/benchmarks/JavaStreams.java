package benchmarks;

import java.util.stream.*;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import static benchmarks.Settings.fillArray;
import java.util.*;
import com.google.common.collect.*;
import static com.google.common.collect.Streams.stream;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class JavaStreams {
   public long[] v, vHi, vLo, vFaZ, vZaF;
   public long vLimit;

   @Setup
   public void setUp() {
      v    = fillArray(Settings.v_s, true);
      vHi  = fillArray(Settings.vHi_s, true);
      vLo  = fillArray(Settings.vLo_s, true);
      vFaZ = fillArray(Settings.vFaZ_s, false);
      vZaF = fillArray(Settings.vZaF_s, false);
      vLimit = Settings.vLimit_s;
   }

   // @TearDown
   // public void check() { 
   //    assert sum() == 450000000L;
   //    assert sumOfSquares() == 2850000000L;
   //    assert sumOfSquaresEven() == 1200000000L;
   //    assert cart() == 2025000000L;
   //    assert mapsMegamorphic() == 2268000000000L;
   //    assert filtersMegamorphic() == 170000000L;
   //    assert flatMapTake() == 405000000L;
   //    assert dotProduct() == 285000000L;
   //    assert flatMapAfterZip() == 1499850000000L;
   //    assert zipAfterFlatMap() == 99999990000000L;
   //    assert zipFilterFilter() == 64000000L;
   //    assert zipFlatMapFlatMap() == 315000000L;
   // }

   @Benchmark
   public long sum() {
      long ret = LongStream.of(v)
         .sum();

      return ret;
   }

   @Benchmark
   public long sumOfSquares() {
      long ret = LongStream.of(v)
         .map(d -> d * d)
         .sum();

      return ret;
   }

   @Benchmark
   public long sumOfSquaresEven() {
      long ret = LongStream.of(v)
         .filter(x -> x % 2 == 0)
         .map(x -> x * x)
         .sum();

      return ret;
   }

   @Benchmark
   public long cart() {
      long ret = LongStream.of(vHi)
         .flatMap(d -> LongStream.of(vLo).map(dP -> dP * d))
         .sum();

      return ret;
   }

   @Benchmark
   public long mapsMegamorphic() {
      long ret = LongStream.of(v)
         .map(d -> d * 1)
         .map(d -> d * 2)
         .map(d -> d * 3)
         .map(d -> d * 4)
         .map(d -> d * 5)
         .map(d -> d * 6)
         .map(d -> d * 7)
         .sum();

      return ret;
   }

   @Benchmark
   public long filtersMegamorphic() {
      long ret = LongStream.of(v)
         .filter(d -> d > 1)
         .filter(d -> d > 2)
         .filter(d -> d > 3)
         .filter(d -> d > 4)
         .filter(d -> d > 5)
         .filter(d -> d > 6)
         .filter(d -> d > 7)
         .sum();

      return ret;
   }

   @Benchmark
   public long flatMapTake() {
      long ret = LongStream.of(vHi).flatMap(x -> LongStream.of(vLo).map(dP -> dP * x))
         .limit(vLimit)
         .sum();
         
      return ret;
   }

   // For pipelines with Zip we use Guava
   // Zip is considered experimental in Guava (Beta)
   // All the following are boxing integers unfortunately since zip is not specialized 
   // https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/Streams.java#L303
   @Benchmark
   public long dotProduct() {
      long ret = Streams.zip(
            LongStream.of(vHi).mapToObj(Long::valueOf),
            LongStream.of(vHi).mapToObj(Long::valueOf),
            (arg1, arg2) -> arg1 + arg2)
         .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long flatMapAfterZip() {
      long ret = Streams.zip(
         LongStream.of(vFaZ).mapToObj(Long::valueOf),
         LongStream.of(vFaZ).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .flatMap(d -> LongStream.of(vFaZ).map(dP -> dP * d).mapToObj(Long::valueOf))
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long zipAfterFlatMap() {
      long ret = Streams.zip(
         LongStream.of(vZaF).flatMap(d -> LongStream.of(vZaF).map(dP -> dP * d)).mapToObj(Long::valueOf),
         LongStream.of(vZaF).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long zipFilterFilter() {
      long ret = Streams.zip(
         LongStream.of(v).filter(x -> x > 7).mapToObj(Long::valueOf),
         LongStream.of(vHi).filter(x -> x > 5).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long zipFlatMapFlatMap() {
      long ret = Streams.zip(
         LongStream.of(v).flatMap(d -> LongStream.of(vLo).map(dP -> dP * d)).mapToObj(Long::valueOf),
         LongStream.of(vLo).flatMap(d -> LongStream.of(v).map(dP -> dP * d)).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .limit(vLimit)
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }
}

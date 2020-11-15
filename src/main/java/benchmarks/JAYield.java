package benchmarks;

import org.jayield.primitives.lng.*;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;
import static benchmarks.Settings.fillArray;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class JAYield {
   public long[] v, vHi, vLo, vFaZ, vZaF;
   public int vLimit;

   @Setup
   public void setUp() {
      v    = fillArray(Settings.v_s, true);
      vHi  = fillArray(Settings.vHi_s, true);
      vLo  = fillArray(Settings.vLo_s, true);
      vFaZ = fillArray(Settings.vFaZ_s, false);
      vZaF = fillArray(Settings.vZaF_s, false);
      vLimit = Settings.vLimit_s;
   }

   @Benchmark
   public long sum() {
      long ret = LongQuery.of(v)
         .sum();
      
      return ret;
   }

   @Benchmark
   public long sumOfSquares() {
      long ret = LongQuery.of(v)
         .map(d -> d * d)
         .sum();

      return ret;
   }

   @Benchmark
   public long sumOfSquaresEven() {
      long ret = LongQuery.of(v)
         .filter(x -> x % 2 == 0)
         .map(x -> x * x)
         .sum();

      return ret;
   }

   @Benchmark
   public long cart() {
      long ret = LongQuery.of(v)
         .flatMap(d -> LongQuery.of(vLo).map(dP -> dP * d))
         .sum();

      return ret;
   }

   @Benchmark
   public long mapsMegamorphic() {
      long ret = LongQuery.of(v)
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
      long ret = LongQuery.of(v)
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
      long ret = LongQuery.of(v).flatMap(x -> LongQuery.of(vLo).map(dP -> dP * x))
         .limit(vLimit)
         .sum();

      return ret;
   }
   
   public long dotProduct() {
      long ret = 
            LongQuery.of(vHi).zip(
            LongQuery.of(vHi), (arg1, arg2) -> arg1 + arg2)
         .sum();

      return ret;
   }

   @Benchmark
   public long flatMapAfterZip() {
      long ret = LongQuery.of(vFaZ).zip(
         LongQuery.of(vFaZ),
         (arg1, arg2) -> arg1 + arg2)
      .flatMap(d -> LongQuery.of(vFaZ).map(dP -> dP * d))
      .sum();

      return ret;
   }

   @Benchmark
   public long zipAfterFlatMap() {
      long ret = LongQuery.of(vZaF).flatMap(d -> LongQuery.of(vZaF).map(dP -> dP * d)).zip(
         LongQuery.of(vZaF),
         (arg1, arg2) -> arg1 + arg2)
      .sum();

      return ret;
   }

   @Benchmark
   public long zipFilterFilter() {
      long ret = LongQuery.of(v).filter(x -> x > 7).zip(
         LongQuery.of(vHi).filter(x -> x > 5),
         (arg1, arg2) -> arg1 + arg2)
      .sum();

      return ret;
   }

   @Benchmark
   public long zipFlatMapFlatMap() {
      long ret = LongQuery.of(v).flatMap(d -> LongQuery.of(vLo).map(dP -> dP * d)).zip(
         LongQuery.of(vLo).flatMap(d -> LongQuery.of(v).map(dP -> dP * d)),
         (arg1, arg2) -> arg1 + arg2)
      .limit(vLimit)
      .sum();

      return ret;
   }
}

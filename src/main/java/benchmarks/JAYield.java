package benchmarks;

import org.jayield.primitives.intgr.*;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;
import static benchmarks.Settings.fillArray;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class JAYield {
   public int[] v, vHi, vLo, vFaZ, vZaF;
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
   public int sum() {
      int ret = IntQuery.of(v)
         .sum();
      
      return ret;
   }

   @Benchmark
   public int sumOfSquares() {
      int ret = IntQuery.of(v)
         .map(d -> d * d)
         .sum();

      return ret;
   }

   @Benchmark
   public int sumOfSquaresEven() {
      int ret = IntQuery.of(v)
         .filter(x -> x % 2 == 0)
         .map(x -> x * x)
         .sum();

      return ret;
   }

   @Benchmark
   public int cart() {
      int ret = IntQuery.of(v)
         .flatMap(d -> IntQuery.of(vLo).map(dP -> dP * d))
         .sum();

      return ret;
   }

   @Benchmark
   public int mapsMegamorphic() {
      int ret = IntQuery.of(v)
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
   public int filtersMegamorphic() {
      int ret = IntQuery.of(v)
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
   public int flatMapTake() {
      int ret = IntQuery.of(v).flatMap(x -> IntQuery.of(vLo).map(dP -> dP * x))
         .limit(vLimit)
         .sum();

      return ret;
   }
   
   public int dotProduct() {
      int ret = 
            IntQuery.of(vHi).zip(
            IntQuery.of(vHi), (arg1, arg2) -> arg1 + arg2)
         .sum();

      return ret;
   }

   @Benchmark
   public int flatMapAfterZip() {
      int ret = IntQuery.of(vFaZ).zip(
         IntQuery.of(vFaZ),
         (arg1, arg2) -> arg1 + arg2)
      .flatMap(d -> IntQuery.of(vFaZ).map(dP -> dP * d))
      .sum();

      return ret;
   }

   @Benchmark
   public int zipAfterFlatMap() {
      int ret = IntQuery.of(vZaF).flatMap(d -> IntQuery.of(vZaF).map(dP -> dP * d)).zip(
         IntQuery.of(vZaF),
         (arg1, arg2) -> arg1 + arg2)
      .sum();

      return ret;
   }

   @Benchmark
   public int zipFilterFilter() {
      int ret = IntQuery.of(v).filter(x -> x > 7).zip(
         IntQuery.of(vHi).filter(x -> x > 5),
         (arg1, arg2) -> arg1 + arg2)
      .sum();

      return ret;
   }

   @Benchmark
   public int zipFlatMapFlatMap() {
      int ret = IntQuery.of(v).flatMap(d -> IntQuery.of(vLo).map(dP -> dP * d)).zip(
         IntQuery.of(vLo).flatMap(d -> IntQuery.of(v).map(dP -> dP * d)),
         (arg1, arg2) -> arg1 + arg2)
      .limit(vLimit)
      .sum();

      return ret;
   }
}

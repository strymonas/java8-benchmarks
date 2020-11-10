package benchmarks;

import org.jayield.primitives.intgr.*;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class JAYield {
   private static int v_s    =  Integer.getInteger("benchmark.v"   , 100000000);
   private static int vHi_s  =  Integer.getInteger("benchmark.vHi" , 10000000);
   private static int vLo_s  =  Integer.getInteger("benchmark.vLo" , 10);
   private static int vFaZ_s =  Integer.getInteger("benchmark.vFaZ", 10000);
   private static int vZaF_s =  Integer.getInteger("benchmark.vZaF", 10000000);
   
   public int[] v, vHi, vLo, vFaZ, vZaF;

   public int[] fillArray(int range, boolean mod){
      int[] array = new int[range];
      for (int i = 0; i < range; i++) {
         if(mod) array[i] = i % 10;
         else array[i] = i;
      }
      return array;
   }

   @Setup
   public void setUp() {
      v    = fillArray(v_s, true);
      vHi  = fillArray(vHi_s, true);
      vLo  = fillArray(vLo_s, true);
      vFaZ = fillArray(vFaZ_s, false);
      vZaF = fillArray(vZaF_s, false);
   }

   @Benchmark
   public int sum_jayield() {
      int sum = IntQuery.of(v)
         .sum();
      return sum;
   }

   @Benchmark
   public int sumOfSquares_jayield() {
      int sum = IntQuery.of(v)
         .map(d -> d * d)
         .sum();
      return sum;
   }

   @Benchmark
   public int sumOfSquaresEven_jayield() {
      int sum = IntQuery.of(v)
         .filter(x -> x % 2 == 0)
         .map(x -> x * x)
         .sum();

      return sum;
   }

   @Benchmark
   public int cart_jayield() {
      int cart = IntQuery.of(v)
         .flatMap(d -> IntQuery.of(vLo).map(dP -> dP * d))
         .sum();

      return cart;
   }

   @Benchmark
   public int maps_megamorphic_jayield() {
      int sum = IntQuery.of(v)
         .map(d -> d * 1)
         .map(d -> d * 2)
         .map(d -> d * 3)
         .map(d -> d * 4)
         .map(d -> d * 5)
         .map(d -> d * 6)
         .map(d -> d * 7)
         .sum();
      return sum;
   }

   @Benchmark
   public int filters_megamorphic_jayield() {
      int sum = IntQuery.of(v)
         .filter(d -> d > 1)
         .filter(d -> d > 2)
         .filter(d -> d > 3)
         .filter(d -> d > 4)
         .filter(d -> d > 5)
         .filter(d -> d > 6)
         .filter(d -> d > 7)
         .sum();
      return sum;
   }

   @Benchmark
   public int flatMap_take_jayield() {
      int sum = IntQuery.of(v).flatMap(x -> IntQuery.of(vLo).map(dP -> dP * x))
         .limit(20000000)
         .sum();
      return sum;
   }
   
   public int dotProduct_jayield() {
      int ret = 
            IntQuery.of(vHi).zip(
            IntQuery.of(vHi), (arg1, arg2) -> arg1 + arg2)
         .sum();

      return ret;
   }

   @Benchmark
   public int flatMap_after_zipWith_jayield() {
      int ret = IntQuery.of(vFaZ).zip(
         IntQuery.of(vFaZ),
         (arg1, arg2) -> arg1 + arg2)
      .flatMap(d -> IntQuery.of(vFaZ).map(dP -> dP * d))
      .sum();

      return ret;
   }

   @Benchmark
   public int zipWith_after_flatMap_jayield() {
      int ret = IntQuery.of(vZaF).flatMap(d -> IntQuery.of(vZaF).map(dP -> dP * d)).zip(
         IntQuery.of(vZaF),
         (arg1, arg2) -> arg1 + arg2)
      .sum();

      return ret;
   }

   @Benchmark
   public int zipWith_filter_filter_jayield() {
      int ret = IntQuery.of(v).filter(x -> x > 7).zip(
         IntQuery.of(vHi).filter(x -> x > 5),
         (arg1, arg2) -> arg1 + arg2)
      .sum();

      return ret;
   }

   @Benchmark
   public int zipWith_flat_flat_jayield() {
      int ret = IntQuery.of(v).flatMap(d -> IntQuery.of(vLo).map(dP -> dP * d)).zip(
         IntQuery.of(vLo).flatMap(d -> IntQuery.of(v).map(dP -> dP * d)),
         (arg1, arg2) -> arg1 + arg2)
      .limit(20000000)
      .sum();

      return ret;
   }
}
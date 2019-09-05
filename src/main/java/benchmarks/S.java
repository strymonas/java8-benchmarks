package benchmarks;

import java.util.stream.*;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class S {

   // For general use
   private static int N =  Integer.getInteger("benchmark.N", 100000000);

   // For cartesian product operations
   private static int N_outer =  Integer.getInteger("benchmark.N_outer", 10000000);
   private static int N_inner =  Integer.getInteger("benchmark.N_inner", 10);

   public int[] v, v_outer, v_inner;

   public int[] fillArray(int range){
      int[] array = new int[range];
      for (int i = 0; i < range; i++) {
         array[i] = i % 10;
      }
      return array;
   }

   @Setup
   public void setUp() {
      v  = fillArray(N);
      v_outer = fillArray(N_outer);
      v_inner = fillArray(N_inner);
   }

   @Benchmark
   public int sum_baseline() {
      int acc = 0;
      for (int i =0 ; i < v.length ; i++) {
         acc += v[i];
      }
      return acc;
   }

   @Benchmark
   public int sumOfSquares_baseline() {
      int acc = 0;
      for (int i =0 ; i < v.length ; i++) {
         acc += v[i] * v[i];
      }
      return acc;
   }

   @Benchmark
   public int sumOfSquaresEven_baseline() {
      int acc = 0;
      for (int i =0 ; i < v.length ; i++) {
         if (v[i] % 2 == 0)
         acc += v[i] * v[i];
      }
      return acc;
   }

   @Benchmark
   public int cart_baseline() {
      int cart = 0;
      for (int d = 0 ; d < v_outer.length ; d++) {
         for (int dp = 0 ; dp < v_inner.length ; dp++){
            cart += v_outer[d] * v_inner[dp];
         }
      }
      return cart;
   }

   @Benchmark
   public int sum_java8() {
      int sum = IntStream.of(v)
      .sum();
      return sum;
   }

   @Benchmark
   public int sumOfSquares_java8() {
      int sum = IntStream.of(v)
      .map(d -> d * d)
      .sum();
      return sum;
   }

   @Benchmark
   public int sumOfSquaresEven_java8() {
      int sum = IntStream.of(v)
      .filter(x -> x % 2 == 0)
      .map(x -> x * x)
      .sum();

      return sum;
   }

   @Benchmark
   public int cart_java8() {
      int cart = IntStream.of(v_outer)
      .flatMap(d -> IntStream.of(v_inner).map(dP -> dP * d))
      .sum();

      return cart;
   }

   @Benchmark
   public int maps_megamorphic_java8() {
      int sum = IntStream.of(v)
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
   public int maps_megamorphic_baseline() {
      int acc = 0;
      for (int i =0 ; i < v.length ; i++) {
         acc += v[i] *1*2*3*4*5*6*7;
      }
      return acc;
   }

   @Benchmark
   public int filters_megamorphic_java8() {
      int sum = IntStream.of(v)
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
   public int filters_megamorphic_baseline() {
      int acc = 0;
      for (int i =0 ; i < v.length ; i++) {
         if (v[i] > 1 && v[i] > 2 && v[i] > 3 && v[i] > 4 && v[i] > 5 && v[i] > 6 && v[i] > 7) {
            acc += v[i];
         }
      }
      return acc;
   }

   //   @Benchmark
   //   public int dotProduct_java8() {
   //
   //   }
   //
   //
   //   @Benchmark
   //   public int flatMap_after_zipWith_java8() {
   //
   //   }
   //
   //
   //   @Benchmark
   //   public int zipWith_after_flatMap_java8() {
   //
   //   }

   @Benchmark
   public int flatMap_take_java8() {
      int sum = IntStream.of(v_outer).flatMap(x -> IntStream.of(v_inner).map(dP -> dP * x))
      .limit(20000000)
      .sum();
      return sum;
   }

   @Benchmark
   public int flatMap_take_baseline() {
      int sum = 0;
      int n = 0;
      boolean flag = true;
      for (int d = 0 ; d < v_outer.length && flag ; d++) {
         for (int dp = 0 ; dp < v_inner.length && flag ; ){
            sum += v_outer[d] * v_inner[dp];
            dp++;
            n++;
            if (n == 20000000)
               flag = false;
         }
      }
      return sum;
   }
}

package benchmarks;

import java.util.stream.*;
import com.google.common.collect.*;
import static com.google.common.collect.Streams.stream;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class Guava {
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

   // Zip is considered experimental in Guava (Beta)
   // All the following are boxing integers unfortunately since zip is not specialized 
   // https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/Streams.java#L303
   public int dotProduct_guava() {
      int ret = Streams.zip(
            IntStream.of(vHi).mapToObj(Integer::valueOf),
            IntStream.of(vHi).mapToObj(Integer::valueOf),
            (arg1, arg2) -> arg1 + arg2)
         .collect(Collectors.summingInt((Integer::intValue)));

      return ret;
   }

   @Benchmark
   public int flatMap_after_zipWith_guava() {
      int ret = Streams.zip(
         IntStream.of(vFaZ).mapToObj(Integer::valueOf),
         IntStream.of(vFaZ).mapToObj(Integer::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .flatMap(d -> IntStream.of(vFaZ).map(dP -> dP * d).mapToObj(Integer::valueOf))
      .collect(Collectors.summingInt((Integer::intValue)));

      return ret;
   }

   @Benchmark
   public int zipWith_after_flatMap_guava() {
      int ret = Streams.zip(
         IntStream.of(vZaF).flatMap(d -> IntStream.of(vZaF).map(dP -> dP * d)).mapToObj(Integer::valueOf),
         IntStream.of(vZaF).mapToObj(Integer::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .collect(Collectors.summingInt((Integer::intValue)));

      return ret;
   }

   @Benchmark
   public int zipWith_filter_filter_guava() {
      int ret = Streams.zip(
         IntStream.of(v).filter(x -> x > 7).mapToObj(Integer::valueOf),
         IntStream.of(vHi).filter(x -> x > 5).mapToObj(Integer::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .collect(Collectors.summingInt((Integer::intValue)));

      return ret;
   }

   @Benchmark
   public int zipWith_flat_flat_guava() {
      int ret = Streams.zip(
         IntStream.of(v).flatMap(d -> IntStream.of(vLo).map(dP -> dP * d)).mapToObj(Integer::valueOf),
         IntStream.of(vLo).flatMap(d -> IntStream.of(v).map(dP -> dP * d)).mapToObj(Integer::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .limit(20000000)
      .collect(Collectors.summingInt((Integer::intValue)));

      return ret;
   }
}

package benchmarks;

import java.util.stream.*;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class JavaBaseline {
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
      v    = fillArray(Settings.v_s, true);
      vHi  = fillArray(Settings.vHi_s, true);
      vLo  = fillArray(Settings.vLo_s, true);
      vFaZ = fillArray(Settings.vFaZ_s, false);
      vZaF = fillArray(Settings.vZaF_s, false);
   }

   @Benchmark
   public int sum() {
      int ret = 0;
      for (int i =0 ; i < v.length ; i++) {
         ret += v[i];
      }
      return ret;
   }

   @Benchmark
   public int sumOfSquares() {
      int ret = 0;
      for (int i =0 ; i < v.length ; i++) {
         ret += v[i] * v[i];
      }
      return ret;
   }

   @Benchmark
   public int sumOfSquaresEven() {
      int ret = 0;
      for (int i =0 ; i < v.length ; i++) {
         if (v[i] % 2 == 0)
         ret += v[i] * v[i];
      }
      return ret;
   }

   @Benchmark
   public int cart() {
      int ret = 0;
      for (int d = 0 ; d < vHi.length ; d++) {
         for (int dp = 0 ; dp < vLo.length ; dp++){
            ret += vHi[d] * vLo[dp];
         }
      }
      return ret;
   }

   @Benchmark
   public int mapsMegamorphic() {
      int ret = 0;
      for (int i =0 ; i < v.length ; i++) {
         ret += v[i] *1*2*3*4*5*6*7;
      }
      return ret;
   }

   @Benchmark
   public int filtersMegamorphic() {
      int ret = 0;
      for (int i =0 ; i < v.length ; i++) {
         if (v[i] > 1 && v[i] > 2 && v[i] > 3 && v[i] > 4 && v[i] > 5 && v[i] > 6 && v[i] > 7) {
            ret += v[i];
         }
      }
      return ret;
   }

   @Benchmark
   public int flatMapTake() {
      int ret = 0;
      int n = 0;
      boolean flag = true;
      for (int d = 0 ; d < vHi.length && flag ; d++) {
         for (int dp = 0 ; dp < vLo.length && flag ; ){
            ret += vHi[d] * vLo[dp];
            dp++;
            n++;
            if (n == 20000000)
               flag = false;
         }
      }
      return ret;
   }

   // TODO
   // @Benchmark
   // public int dotProduct() {

   // }

   // TODO
   // @Benchmark
   // public int flatMapAfterZip() {

   // }

   // TODO
   // @Benchmark
   // public int zipAfterFlatMap() {

   // }

   // TODO
   // @Benchmark
   // public int zipFilterFilter() {

   // }

   // TODO
   // @Benchmark
   // public int zipFlatFlat() {

   // }

}

package benchmarks;

import java.util.stream.*;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;
import static benchmarks.Settings.fillArray;


@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class JavaBaseline {
   public long[] v, vHi, vLo, vFaZ, vZaF;
   public int vLimit;

   @Setup
   public void setUp() {
      v      = fillArray(Settings.v_s, true);
      vHi    = fillArray(Settings.vHi_s, true);
      vLo    = fillArray(Settings.vLo_s, true);
      vFaZ   = fillArray(Settings.vFaZ_s, false);
      vZaF   = fillArray(Settings.vZaF_s, false);
      vZaF   = fillArray(Settings.vZaF_s, false);
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
      long ret = 0L;
      for (int i =0 ; i < v.length ; i++) {
         ret += v[i];
      }
      return ret;
   }

   @Benchmark
   public long sumOfSquares() {
      long ret = 0L;
      for (int i =0 ; i < v.length ; i++) {
         ret += v[i] * v[i];
      }
      return ret;
   }

   @Benchmark
   public long sumOfSquaresEven() {
      long ret = 0L;
      for (int i =0 ; i < v.length ; i++) {
         if (v[i] % 2 == 0)
         ret += v[i] * v[i];
      }
      return ret;
   }

   @Benchmark
   public long cart() {
      long ret = 0L;
      for (int d = 0 ; d < vHi.length ; d++) {
         for (int dp = 0 ; dp < vLo.length ; dp++){
            ret += vHi[d] * vLo[dp];
         }
      }
      return ret;
   }

   @Benchmark
   public long mapsMegamorphic() {
      long ret = 0L;
      for (int i =0 ; i < v.length ; i++) {
         ret += v[i] *1*2*3*4*5*6*7;
      }
      return ret;
   }

   @Benchmark
   public long filtersMegamorphic() {
      long ret = 0L;
      for (int i =0 ; i < v.length ; i++) {
         if (v[i] > 1 && v[i] > 2 && v[i] > 3 && v[i] > 4 && v[i] > 5 && v[i] > 6 && v[i] > 7) {
            ret += v[i];
         }
      }
      return ret;
   }

   @Benchmark
   public long flatMapTake() {
      long ret = 0L;
      int n = 0;
      boolean flag = true;
      for (int d = 0 ; d < vHi.length && flag ; d++) {
         for (int dp = 0 ; dp < vLo.length && flag ; ){
            ret += vHi[d] * vLo[dp];
            dp++;
            n++;
            if (n == vLimit)
               flag = false;
         }
      }
      return ret;
   }

   @Benchmark
   public long dotProduct() {
      long ret = 0L;
      for (int counter = 0; counter < vHi.length; counter++) {
         ret += vHi[counter] * vHi[counter];
      }
      return ret;
   }

   @Benchmark
   public long flatMapAfterZip() {
      long ret = 0L;
      for (int counter1 = 0; counter1 < vFaZ.length; counter1++) {
         long item1 = vFaZ[counter1] + vFaZ[counter1];
         for (int counter2 = 0; counter2 < vFaZ.length; counter2++) {
            long item2 = vFaZ[counter2];
            ret +=  item2 + item1;
         }
      }
      return ret;
   }

   @Benchmark
   public long zipAfterFlatMap() {
      long ret = 0L;
      int index1 =  0;
      int index2 =  0;
      boolean flag1 = (index1 <= vZaF.length - 1);
      while (flag1 && (index2 <= vZaF.length - 1)) {
         long el2 = vZaF[index2];
         index2 += 1;
         int index_zip = 0;
         while (flag1 && (index_zip <= vZaF.length - 1)) {
            long el1 = vZaF[index_zip];
            index_zip += 1;
            long elz = vZaF[index1];
            index1 += 1;
            flag1 = (index1 <= vZaF.length - 1);
            ret = ret + elz + el1 + el2;
         }
      }
      return ret;
   }

   @Benchmark
   public long zipFilterFilter() {
      long ret = 0L;
      int counter1 =  0;
      int counter2 =  0;
      long arr1[] = v;
      long arr2[] = vHi;
      while (counter1 < arr1.length && counter2 < arr2.length) {
         while(!(arr1[counter1] > 7 && counter1 < arr1.length)) {
            counter1++;
         }
         if(counter1 < arr1.length){
            long item2 = arr2[counter2];
            if(item2 > 5) {
               ret = ret + arr1[counter1] + item2;
               counter1++;
            }
            counter2++;
         }
      }
      return ret;  
   }

   @Benchmark
   public long zipFlatMapFlatMap() {
      long arr1[] = v;
      long arr2[] = vLo;
      int index11 = 0;
      int index12 = 0;
      int index21 = 0;
      int index22 = 0;
      long ret = 0L;
      int taken = 0;
      int toTake = vLimit;
      int size1 = arr1.length;
      int size2 = arr2.length;
      boolean goon = true;
      while(index11 < size1 && taken < toTake && goon) {
         index12 = 0;
         while(index12 < size2 && taken < toTake && goon) {
            long el1 = arr1[index11] * arr2[index12];
            if(index22 > size1) {
               index21++; 
               index22 = 0;
            }
            if(index21 >= size2) {
               goon = false;
            }
            else {
               if(index22 < size1){
                  ret = ret + el1 + arr2[index21] - arr1[index22];
                  taken++;
                  index22++;
               }
            }
            index12++;
         }
         index11++;
      }
      return ret;
   }
}

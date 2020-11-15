package benchmarks;

public class Settings {
   static int v_s      =  Integer.getInteger("benchmark.v"     , 100000000);
   static int vHi_s    =  Integer.getInteger("benchmark.vHi"   , 10000000);
   static int vLo_s    =  Integer.getInteger("benchmark.vLo"   , 10);
   static int vFaZ_s   =  Integer.getInteger("benchmark.vFaZ"  , 10000);
   static int vZaF_s   =  Integer.getInteger("benchmark.vZaF"  , 10000000);
   static int vLimit_s =  Integer.getInteger("benchmark.vLimit", 20000000);

   static public long[] fillArray(int range, boolean mod){
      long[] array = new long[range];
      for (int i = 0; i < range; i++) {
         if(mod) array[i] = i % 10;
         else array[i] = i;
      }
      return array;
   }

   /**
      Expected Results
    */
   static long sum                 = 450000000L;
   static long sumOfSquares        = 2850000000L;
   static long sumOfSquaresEven    = 1200000000L;
   static long cart                = 2025000000L;
   static long mapsMegamorphic     = 2268000000000L;
   static long filtersMegamorphic  = 170000000L;
   static long flatMapTake         = 405000000L;
   static long dotProduct          = 285000000L;
   static long flatMapAfterZip     = 1499850000000L;
   static long zipAfterFlatMap     = 99999990000000L;
   static long zipFilterFilter     = 64000000L;
   static long zipFlatFlat         = 315000000L;
}
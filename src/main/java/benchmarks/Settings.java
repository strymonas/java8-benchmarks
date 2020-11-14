package benchmarks;

public class Settings {
   static int v_s      =  Integer.getInteger("benchmark.v"     , 100000000);
   static int vHi_s    =  Integer.getInteger("benchmark.vHi"   , 10000000);
   static int vLo_s    =  Integer.getInteger("benchmark.vLo"   , 10);
   static int vFaZ_s   =  Integer.getInteger("benchmark.vFaZ"  , 10000);
   static int vZaF_s   =  Integer.getInteger("benchmark.vZaF"  , 10000000);
   static int vLimit_s =  Integer.getInteger("benchmark.vLimit", 20000000);

   static public int[] fillArray(int range, boolean mod){
      int[] array = new int[range];
      for (int i = 0; i < range; i++) {
         if(mod) array[i] = i % 10;
         else array[i] = i;
      }
      return array;
   }

   /**
      Expected Results
    */
   //    static int sum                 = 450000000;
   //    static int sumOfSquares        = 2850000000;
   //    static int sumOfSquaresEven    = 1200000000;
   //    static int cart                = 2025000000;
   //    static int mapsMegamorphic     = 2268000000000;
   //    static int filtersMegamorphic  = 170000000;
   //    static int flatMapTake         = 405000000;
   //    static int dotProduct          = 285000000;
   //    static int flatMapAfterZip     = 1499850000000;
   //    static int zipAfterFlatMap     = 99999990000000;
   //    static int zipFilterFilter     = 64000000;
   //    static int zipFlatFlat         = 315000000;
}
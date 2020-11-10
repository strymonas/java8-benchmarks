package benchmarks;

public class Settings {
   static int v_s    =  Integer.getInteger("benchmark.v"   , 100000000);
   static int vHi_s  =  Integer.getInteger("benchmark.vHi" , 10000000);
   static int vLo_s  =  Integer.getInteger("benchmark.vLo" , 10);
   static int vFaZ_s =  Integer.getInteger("benchmark.vFaZ", 10000);
   static int vZaF_s =  Integer.getInteger("benchmark.vZaF", 10000000);
}
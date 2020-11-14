package benchmarks;

import java.util.stream.*;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import static benchmarks.Settings.fillArray;
import java.util.*;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class JavaStreams {
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
      int ret = IntStream.of(v)
         .sum();

      return ret;
   }

   @Benchmark
   public int sumOfSquares() {
      int ret = IntStream.of(v)
         .map(d -> d * d)
         .sum();

      return ret;
   }

   @Benchmark
   public int sumOfSquaresEven() {
      int ret = IntStream.of(v)
         .filter(x -> x % 2 == 0)
         .map(x -> x * x)
         .sum();

      return ret;
   }

   @Benchmark
   public int cart() {
      int ret = IntStream.of(vHi)
         .flatMap(d -> IntStream.of(vLo).map(dP -> dP * d))
         .sum();

      return ret;
   }

   @Benchmark
   public int mapsMegamorphic() {
      int ret = IntStream.of(v)
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
      int ret = IntStream.of(v)
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
      int ret = IntStream.of(vHi).flatMap(x -> IntStream.of(vLo).map(dP -> dP * x))
         .limit(vLimit)
         .sum();
         
      return ret;
   }
}

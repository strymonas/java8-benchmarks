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
   public long[] v, vHi, vLo, vFaZ, vZaF;
   public long vLimit;

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
   public long sum() {
      long ret = LongStream.of(v)
         .sum();

      return ret;
   }

   @Benchmark
   public long sumOfSquares() {
      long ret = LongStream.of(v)
         .map(d -> d * d)
         .sum();

      return ret;
   }

   @Benchmark
   public long sumOfSquaresEven() {
      long ret = LongStream.of(v)
         .filter(x -> x % 2 == 0)
         .map(x -> x * x)
         .sum();

      return ret;
   }

   @Benchmark
   public long cart() {
      long ret = LongStream.of(vHi)
         .flatMap(d -> LongStream.of(vLo).map(dP -> dP * d))
         .sum();

      return ret;
   }

   @Benchmark
   public long mapsMegamorphic() {
      long ret = LongStream.of(v)
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
   public long filtersMegamorphic() {
      long ret = LongStream.of(v)
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
   public long flatMapTake() {
      long ret = LongStream.of(vHi).flatMap(x -> LongStream.of(vLo).map(dP -> dP * x))
         .limit(vLimit)
         .sum();
         
      return ret;
   }
}

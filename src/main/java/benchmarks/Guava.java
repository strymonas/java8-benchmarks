package benchmarks;

import java.util.stream.*;
import com.google.common.collect.*;
import static com.google.common.collect.Streams.stream;
import static benchmarks.Settings.fillArray;

import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import java.util.*;

@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.AverageTime)
@State(Scope.Thread)
@Fork(1)
public class Guava {
   public long[] v, vHi, vLo, vFaZ, vZaF;
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

   // Zip is considered experimental in Guava (Beta)
   // All the following are boxing integers unfortunately since zip is not specialized 
   // https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/Streams.java#L303
   public long dotProduct() {
      long ret = Streams.zip(
            LongStream.of(vHi).mapToObj(Long::valueOf),
            LongStream.of(vHi).mapToObj(Long::valueOf),
            (arg1, arg2) -> arg1 + arg2)
         .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long flatMapAfterZip() {
      long ret = Streams.zip(
         LongStream.of(vFaZ).mapToObj(Long::valueOf),
         LongStream.of(vFaZ).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .flatMap(d -> LongStream.of(vFaZ).map(dP -> dP * d).mapToObj(Long::valueOf))
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long zipAfterFlatMap() {
      long ret = Streams.zip(
         LongStream.of(vZaF).flatMap(d -> LongStream.of(vZaF).map(dP -> dP * d)).mapToObj(Long::valueOf),
         LongStream.of(vZaF).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long zipFilterFilter() {
      long ret = Streams.zip(
         LongStream.of(v).filter(x -> x > 7).mapToObj(Long::valueOf),
         LongStream.of(vHi).filter(x -> x > 5).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }

   @Benchmark
   public long zipFlatMapFlatMap() {
      long ret = Streams.zip(
         LongStream.of(v).flatMap(d -> LongStream.of(vLo).map(dP -> dP * d)).mapToObj(Long::valueOf),
         LongStream.of(vLo).flatMap(d -> LongStream.of(v).map(dP -> dP * d)).mapToObj(Long::valueOf),
         (arg1, arg2) -> arg1 + arg2)
      .limit(vLimit)
      .collect(Collectors.summingLong((Long::longValue)));

      return ret;
   }
}

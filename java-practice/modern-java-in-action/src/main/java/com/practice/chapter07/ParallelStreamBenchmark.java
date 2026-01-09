package com.practice.chapter07;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime) // 벤치마크 대상 메서드를 실행하는 데 걸린 평균 시간 측정
@OutputTimeUnit(TimeUnit.MILLISECONDS) // 벤치마크 결과를 밀리초 단위로 출력
@Fork(value = 2, jvmArgs = { "-Xms4G", "-Xmx4G" }) // 4Gb의 힙 공간을 제공한 환경에서 두 번 벤치마크를 수행해 결과의 신뢰성 확보
public class ParallelStreamBenchmark {

    private static final long N = 10_000_000L;

    // @Benchmark
    public long sequentialSum() {
        return Stream.iterate(1L, i -> i + 1)
                .limit(N)
                .reduce(0L, Long::sum);
    }

    // @Benchmark
    public long iterativeSum() {
        long result = 0;
        for (long i = 1L; i <= N; i++) {
            result += i;
        }
        return result;
    }

    /**
     * 병렬 버전이 CPU를 활용하지 못하고 순차 버전에 비해 느린 이유
     * - 반복 결과로 박싱된 객체가 만들어지므로 숫자를 더하려면 언박싱을 해야 한다.
     * - 반복 작업은 병렬로 수행할 수 있는 독립 단위로 나누기가 어렵다.
     * 이전 결연산의 결과에 따라 다음 함수의 입력이 달라지기 때문에 iterate 연상을 청크로 분할하기가 어렵다.
     * 결국 순차처리 방식과 크게 다른 점이 없으므로 스레드를 할당하는 오버헤드만 증가하게 된다.
     */
    // @Benchmark
    public long parallelSum() {
        return Stream.iterate(1L, i -> i + 1)
                .parallel()
                .limit(N)
                .reduce(0L, Long::sum);
    }

    /**
     * LongStream.rangeClosed는 기본형 long을 직접 사용하므로 박싱과 언박싱 오버헤드가 사라진다.
     * 또한 쉽게 청크로 분할할 수 있는 숫자 범위를 생산한다.
     */
    @Benchmark
    public long rangedSum() {
        return LongStream.rangeClosed(1, N)
                .reduce(0L, Long::sum);
    }

    @Benchmark
    /**
     * 병렬화를 이용하려면 스트림을 재귀적으로 분할해야 하고, 각 서브스트림을 서로 다른 스레드의 리듀싱 연산으로 할당하고,
     * 이들 결과를 하나의 값으로 합쳐야 한다.
     * 멀티코어 간의 데이터 이동은 생각보다 비싸기 떄문에 코어 간에 데이터 전송 시간보다 훨씬 오래 걸리는 작업만
     * 병렬로 다른 코어에서 수행하는 것이 바람직하다.
     */
    public long parallelRangedSum() {
        return LongStream.rangeClosed(1, N)
                .parallel()
                .reduce(0, Long::sum);
    }

    // 매번 벤치마크를 실행한 다음에는 가비지 컬렉터 동작 시도
    @TearDown(Level.Invocation)
    public void tearDown() {
        System.gc();
    }
}

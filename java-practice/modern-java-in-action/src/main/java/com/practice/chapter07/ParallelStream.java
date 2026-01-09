package com.practice.chapter07;

import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 병렬 스트림이란 각각의 스레드에서 처리할 수 있도록 스트림 요소를 여러 청크로 분할한 스트림이다.
 * 따라서 병렬 스트림을 이용하면 모든 멀티코어 프로세서가 각각의 청크를 처리하도록 할당할 수 있다.
 * 
 * 병렬 스트림 효과적으로 사용하기
 * - 확신이 서지 않으면 직접 측정하라.
 * 언제나 병렬 스트림이 순차 스트림보다 빠른 것은 아니며, 병렬 스트림의 수행 과정은 투명하지 않을 때가 많다.
 * 
 * - 박싱을 주의하라.
 * 자동 박싱과 언박싱은 성능을 크게 저하시킬 수 있는 요소다.
 * 따라서 되도록이면 기본형 특화 스트림을 사용하는 것이 좋다.
 * 
 * - 순차 스트림보다 병렬 스트림에서 성능이 떨어지는 연산이 있다.
 * 특히 limit나 findFirst처럼 요소의 순서에 의존하는 연산을 병렬 스트림에서 수행하려면 비싼 비용을 치러야 한다.
 * 정렬된 스트림에 unordered를 호출하면 비정렬된 스트림을 얻을 수 있다.
 * 스트림에 N개 요소가 있을 때 요소의 순서가 상관없다면 비정렬된 스트림에 limit를 호출하는 것이 더 효율적이다.
 * 
 * - 스트림에서 수행하는 전체 파이프라인 연산 비용을 고려하라.
 * 하나의 요소를 처리하는 데 드는 비용이 높아진다는 것은 병렬 스트림으로 성능을 개선할 수 있는 가능성이 있음을 의미한다.
 * 
 * - 소량의 데이터에서는 병렬 스트림이 도움 되지 않는다.
 * 병렬화 과정에서 생기는 부가 비용을 상쇄할 수 있을 만큼의 이득을 얻지 못하기 때문이다.
 * 
 * - 스트림을 구성하는 자료구조가 적절한지 확인하라.
 * 
 * - 스트림의 특성과 파이프라인의 중간 연산이 스트림의 특성을 어떻게 바꾸는지에 따라 분해 과정의 성능이 달라질 수 있다.
 * 예를 들어 필터 연산이 있으면 스트림의 길이를 예측할 수 없으므로 효과적으로 스트림을 병렬 처리할 수 있을지 알 수 없게 된다.
 * 
 * - 최종 연산 병합 과정(Collector의 combiner 메서드) 비용을 살펴보라.
 * 병합 과정의 비용이 비싸다면 병렬 스트림으로 얻은 성능의 이익이 서브스트림의 부분 결과를 합치는 과정에서 상쇄될 수 있다.
 */
public class ParallelStream {

    public long iterativeSum(long n) {
        long result = 0;
        for (long i = 1L; i <= n; i++) {
            result += i;
        }
        return result;
    }

    public long sequentialSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .reduce(0L, Long::sum);
    }

    /**
     * 순차 스트림에 parallel을 호출해도 스트림 자체에는 아무 변화도 일어나지 않는다.
     * 내부적으로는 parallel을 호출하면 이후 연산이 병렬로 수핸해야 함을 의미하는 불리언 플래그가 설정된다.
     * 반대로 sequential로 병렬 스트림을 순차 스트림으로 바꿀 수 있다.
     * 
     * parallel과 sequential 두 메서드 중 최종적으로 호출된 메서드가 전체 파이프라인에 영향을 미친다.
     */
    public long parallelSum(long n) {
        return Stream.iterate(1L, i -> i + 1)
                .limit(n)
                .parallel()
                .reduce(0L, Long::sum);
    }

    /**
     * * 병렬 스트림에서 사용하는 스레드 풀 설정
     * 병렬 스트림은 내부적으로 ForkJoinPool을 사용한다.
     * 기본적으로 ForkJoinPool은 프로세서 수, 즉 Runtime.getRuntime().availableProcessors()가
     * 반환하는 값에 상응하는 스레드를 갖는다.
     */

    public static class Accumulator {
        public long total = 0;

        public void add(long value) {
            total += value;
        }
    }

    public static long sideEffectSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n).forEach(accumulator::add);
        return accumulator.total;
    }

    /**
     * 본질적으로 순차 실행할 수 있도록 구현되어 있으므로 병렬로 실행하면 문제가 발생한다.
     * 특히 total을 접근할 때마다 데이터 레이스 문제가 일어난다.
     * 
     * 여러 스레드에서 동시에 누적자, 즉 total += value를 실행하면서 문제가 발생한다.
     * total += value는 아토믹 연산(atomic operation)이 아니다.
     */
    public static long sideEffectParallelSum(long n) {
        Accumulator accumulator = new Accumulator();
        LongStream.rangeClosed(1, n)
                .parallel()
                .forEach(accumulator::add);
        return accumulator.total;
    }

    public static void test() {
        System.out.println("--- Tdst parallel stream ---");

        System.out.println("SideEffect sum done in: "
                + ParallelStreamTest.measurePerf(ParallelStream::sideEffectSum, 10_000_000L) + " msecs");

        System.out.println("SideEffect parallel sum done in: "
                + ParallelStreamTest.measurePerf(ParallelStream::sideEffectParallelSum, 10_000_000L) + " msecs");
    }
}

package com.practice.chapter07;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * 포크/조인 프레임워크는 병렬화할 수 있는 작업을 재귀적으로 작은 작업으로 분할한 다음에 서브태스크 각각의 결과를 합쳐서 전체 결과를
 * 만들도록 설계되었다.
 * 포크/조인 프레임워크에서는 서브태스크를 스레드 풀(ForkJoinPool)의 작업자 스레드에 분산 할당하는 ExecutorService
 * 인터페이스를 구현한다.
 * 
 * 스레드 풀을 이용하려면 RecursiveTask<R>의 서브클래스를 만들어야 한다.
 * 여기서 R은 병렬화된 태스크가 생성하는 결과 형식 또는 결과가 없을 때(결과가 없더라도 다른 비지역 구조를 바꿀 수 있다.)는
 * RecursiveAction 형식이다.
 * 
 * 포크/조인 프레임워크를 제대로 사용하는 방법
 * - join 메서드를 태스크에 호출하면 태스크가 생산하는 결과가 준비될 때까지 호출자를 블록시킨다.
 * 그렇지 않으면 각각의 서브태스크가 다른 태스크가 끝나길 기다리는 일이 발생하며 원래 순차 알고리즘보다 느리고 복잡한 프로그램이 되어버릴 수
 * 있다.
 * 
 * - RecursiveTask 내에서는 ForkJoinPool의 invoke 메서드를 사용하지 말아야 한다.
 * 대신 compute나 fork 메서드를 직접 호출할 수 있다.
 * 순차 코드에서 병렬 계산을 시작할 때만 invoke를 사용한다.
 * 
 * - 서브태스크에 fork 메서드를 호출해서 ForkJoinPool의 일정을 조절할 수 있다.
 * 한쪽 작업에는 fork를 호출하는 것보다는 compute를 호출하는 것이 효율적이다.
 * 두 서브태스크의 한 태스크에는 같은 스레드를 재사용할 수 있으므로 풀에서 불필요한 태스크를 할당하는 오버헤드를 피할 수 있다.
 * 
 * - 포크/조인 프레임워크를 이용하는 병렬 계산은 디버깅하기 어렵다.
 * fork라 불리는 다른 스레드에서 compute를 호출하므로 스택 트레이스(stack trace)가 도움이 되지 않는다.
 * 
 * - 포크/조인 프레임워크를 사용하는 것이 순차 처리보다 무조건 빠를 거라는 생각은 버려야 한다.
 * 병렬 처리로 성능을 개선하려면 태스크를 여러 독립적인 서브태스크로 분할할 수 있어야 한다.
 * 각 서브태스크의 실행 시간은 새로운 태스크를 포킹하는 데 드는 시간보다 길어야 한다.
 * 또한 컴파일러 최적화는 병렬 버전보다는 순차 버전에 집중될 수 있다는 사실도 기억하자. (예를 들어 순차 버전에서는 죽은 코드를 분석해서
 * 사용되지 않는 계산은 아예 삭제하는 등의 최적화를 달성하기 쉽다)
 * 
 * 작업 훔치기(work stealing)
 * 작업 훔치기 기법에서는 ForkJoinPool의 모든 스레드를 거의 공정하게 분할한다.
 * 각각의 스레드는 자신에게 할당된 태스크를 포함하는 이중 연결 리스트를 참조하면서 작업이 끝날 때마다 큐의 헤드에서 다른 태스크를 가져와서
 * 작업을 처리한다.
 * 이때 한 스레드는 다른 스레드보다 자신에게 할당된 태스크를 더 빨리 처리할 수 있다.
 * 이때 할일이 없어진 스레드는 유휴 상태로 바뀌는 것이 아니라 다른 스레드 큐의 꼬리(tail)에서 작업을 훔쳐온다.
 * 따라서 태스크의 크기를 작게 나누어야 작업자 스레드 간의 작업부하를 비슷한 수준으로 유지할 수 있다.
 */
public class ForkJoinSumCalculator extends RecursiveTask<Long> {

    private final long[] numbers;
    private final int start;
    private final int end;
    public static final long THRESHOLD = 10_000; // 이 값 이하의 서브태스크는 더 이상 분할할 수 없다.

    // 메인 태스크를 생성할 떄 사용할 공개 생성자
    public ForkJoinSumCalculator(long[] numbers) {
        this(numbers, 0, numbers.length);
    }

    // 서브태스크를 재귀적으로 만들 때 사용할 비공개 생성자
    private ForkJoinSumCalculator(long[] numbers, int start, int end) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
    }

    /**
     * compute 메서드는 태스크를 서브태스크로 분할하는 로직과 더 이상 분할할 수 없을 때 개별 서브태스크의 결과를 생산할 알고리즘을
     * 정의한다.
     * 이 알고리즘은 분할 후 정복(divide-and-conquer) 알고리즘의 병렬화 버전이다.
     */
    @Override
    protected Long compute() {
        int length = end - start;

        if (length <= THRESHOLD) {
            return computeSequentially();
        }

        ForkJoinSumCalculator leftTask = new ForkJoinSumCalculator(numbers, start, start + length / 2);
        leftTask.fork(); // ForkJoinPool의 다른 스레드로 새로 생성한 태스크를 비동기로 실행한다.

        ForkJoinSumCalculator rightTask = new ForkJoinSumCalculator(numbers, start + length / 2, end);

        Long rightResult = rightTask.compute(); // 두 번째 서브태스크를 동기 실행한다. 이때 추가로 분할이 일어날 수 있다.
        Long leftResult = leftTask.join(); // 첫 번째 서브태스크의 결과를 읽거나 아직 결과가 없으면 기다린다.

        return leftResult + rightResult;
    }

    private long computeSequentially() {
        long sum = 0;
        for (int i = start; i < end; i++) {
            sum += i;
        }
        return sum;
    }

    /**
     * 일반적으로 애플리케이션에서는 둘 이상의 ForkJoinPool을 사용하지 않는다.
     * 즉, 소프트웨어의 필요한 곳에서 언제든 가져다 쓸 수 있도록 ForkJoinPool을 한 번만 인스턴스화해서 정적 필드에 싱글턴으로
     * 저장한다.
     * Fork
     * oinPool을 만들면서 인수가 없는 디폴트 생성자를 이용했는데, 이는 JVM에서 이용할 수 있는 모든 프로세서가 자유롭게 풀에 접근할 수
     * 있음을 의미한다.
     * 더 정확하게는 Runtime.availableProcessors의 반환값으로 풀에 사용할 스레드 수를 결정한다.
     */
    @SuppressWarnings("resource")
    public static long forkJoinSum(long n) {
        long[] numbers = LongStream.rangeClosed(1, n).toArray();

        ForkJoinTask<Long> task = new ForkJoinSumCalculator(numbers);

        // invoke 메서드의 반환값은 ForkJoinSumCalculator에서 정의한 테스크의 결과가 된다.

        return new ForkJoinPool().invoke(task);
    }
}

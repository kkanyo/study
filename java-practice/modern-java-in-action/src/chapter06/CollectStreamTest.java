package chapter06;

import static chapter06.PrimeNumbersCollector.isPrime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * 다수준(multilevel)으로 그룸화를 수행할 때 명령형 프로그래밍과 함수형
 * 프로그래밍의 차이점이 더욱 두드러진다.
 * 명령형 코드에서는 문제를 해결하는 과정에서 다중 루프와 조건문을 추가하며
 * 가독성과 유지보수성이 크게 떨어진다.
 * 함수형 프로그래밍에서는 필요한 컬렉터를 쉽게 추가할 수 있다.
 * 
 * 훌륭하게 설계된 함수형 API의 또 다른 장점으로 높은 수준의 조합성과
 * 재사용성을 꼽을 수 있다.
 * collect로 결과를 수집하는 과정을 간단하면서도 유연한 방식으로 정의할
 * 수 있다는 점이 컬렉터의 최대 강점이다.
 * 스트림에 collect를 호출하면 스트림의 요소에 (컬렉터로 파라미터화된)
 * 리듀싱 연산(reducing operation)이 수행된다.
 * 
 * 리듀싱 연산이란 함수형 프로그래밍에서 스트림의 요소를 반복적으로 처리하여
 * 단일 결과값(값, 객체 등)을 만들어내는 연산이다.
 * 
 * Collectors에서 제공하는 메서드의 기능은 크게 세 가지로 구분할 수 있다.
 * - 스트림 요소를 하나의 값으로 리듀스하고 요약
 * - 요소 그룹화
 * - 요소 분할
 */
public class CollectStreamTest {

    public static void main(String[] args) {
        Summarizing.test();

        Grouping.test();

        Partitioning.test();

        ToListCollector.test();

        // Custom collector
        System.out.println("--- Test custom collector ---");

        long fastest = Long.MAX_VALUE;

        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            // Partitioning.partitionPrimes(1_000_000);
            partitionPrimesWithCustomCollector(1_000_000);
            long duration = (System.nanoTime() - start) / 1_000_000;

            if (duration < fastest) {
                fastest = duration;
            }
        }
        System.out.println("Fastest execution done in " + fastest + " msecs");
    }

    // Quiz 6-3
    // 스트림 API와는 달리 직접 구현한 takeWhile 메소드는 적극적(eager)으로 동작한다.
    // 따라서 가능하면 noneMatch 동작과 조화를 이룰 수 있도록
    // 스트림에서 제공하는 게으른 버전의 takeWhile을 사용하는 것이 좋다.
    public static <A> List<A> takeWhile(List<A> list, Predicate<A> p) {
        int i = 0;
        for (A item : list) {
            if (!p.test(item)) {
                return list.subList(0, i);
            }
            i++;
        }
        return list;
    }

    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollector(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(new PrimeNumbersCollector());
    }

    // 코드는 간결하지만 가독성과 재사용성은 떨어진다.
    public static Map<Boolean, List<Integer>> partitionPrimesWithCustomCollectorLambda(int n) {
        return IntStream.rangeClosed(2, n).boxed()
                .collect(
                        () -> new HashMap<Boolean, List<Integer>>() {
                            {
                                put(true, new ArrayList<>());
                                put(false, new ArrayList<>());
                            }
                        },
                        (acc, candidate) -> {
                            acc.get(isPrime(acc.get(true), candidate))
                                    .add(candidate);
                        },
                        (map1, map2) -> {
                            map1.get(true).addAll(map2.get(true));
                            map1.get(false).addAll(map2.get(false));
                        });
    }
}

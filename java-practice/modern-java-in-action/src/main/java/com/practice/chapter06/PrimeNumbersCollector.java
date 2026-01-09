package com.practice.chapter06;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 소수로 나누어떨어지는지 확인해서 대상의 범위를 좁힐 수 있다.
 * 제수(devisor)가 소수가 아니면 소용없으므로 제수를 현재 숫자 이하에서 발견한 소수로 제한할 수 있다.
 * 지금까지 발견한 소수 리스트에 접근해야 하는데 커스텀 컬렉터 클래스로 이 문제를 해결할 수 있다.
 * 
 * filter를 이용해서 대상의 루트보다 작은 소수를 필터링할 수 있다.
 * 하지만 결국 filter는 전체 스트림을 처리한 다음에 결과를 반환하게 된다.
 * 대상의 제곱근보다 큰 소수를 찾으면 검사를 중단함으로써 성능 문제를 해결할 수 있다.
 */
public class PrimeNumbersCollector
        implements Collector<Integer, Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> {

    public static boolean isPrime(List<Integer> primes, int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);

        return primes.stream()
                .takeWhile(i -> i <= candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

    // 두 개의 빈 리스트를 포함하는 맵으로 수집 동작을 시작한다.
    @Override
    public Supplier<Map<Boolean, List<Integer>>> supplier() {
        return () -> new HashMap<Boolean, List<Integer>>() {
            {
                put(true, new ArrayList<Integer>());
                put(false, new ArrayList<Integer>());
            }
        };
    }

    // 지금까지 발견한 소수 리스트를 isPrime 메서드로 전달한다.
    // isPrime 메서드의 결과에 따라 맵에서 알맞은 리스트를 받아 현재 candidate를 추가한다.
    @Override
    public BiConsumer<Map<Boolean, List<Integer>>, Integer> accumulator() {
        return (Map<Boolean, List<Integer>> acc, Integer candidate) -> {
            acc.get(isPrime(acc.get(true), candidate))
                    .add(candidate);
        };
    }

    // 두 번째 맵을 첫 번째 맵에 병합한다.
    @Override
    public BinaryOperator<Map<Boolean, List<Integer>>> combiner() {
        return (Map<Boolean, List<Integer>> map1, Map<Boolean, List<Integer>> map2) -> {
            map1.get(true).addAll(map2.get(true));
            map1.get(false).addAll(map2.get(false));
            return map1;
        };
    }

    // 최종 수집 과정에서 데이터 변환이 필요하지 않으므로 항등 함수를 반환한다.
    @Override
    public Function<Map<Boolean, List<Integer>>, Map<Boolean, List<Integer>>> finisher() {
        return Function.identity();
    }

    // 발견한 소수의 순서에 의미가 있으므로 컬렉터는 IDENTITY_FINISH지만
    // UNORDERED, CONCURRENT는 아니다.
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
    }
}

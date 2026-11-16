package com.practice.chapter06;

import static com.practice.chapter04.Dish.menu;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import com.practice.chapter04.Dish;

/**
 * {@link java.util.stream.Collector}
 * - T는 수집될 스트림 항목의 제네릭 형식이다.
 * - A는 누적자, 즉 수집 과정에서 중간 결과를 누적하는 객체의 형식이다.
 * - R은 수집 연산 결과 객체의 형식(보통 컬렉션 형식)이다.
 */
public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

    /**
     * 새로운 결과 컨테이너 만들기
     * 수집 과정에서 빈 누적자 인스턴스를 만드는 파라미터가 ㅇ벗는 함수다.
     */
    @Override
    public Supplier<List<T>> supplier() {
        return ArrayList::new;
    }

    /**
     * 결과 컨테이너에 요소 추가하기
     * 리듀싱 연산을 수행하는 함수를 반환한다.
     * 스트림에서 n번째 요소를 탐색할 때 두 인수, 즉 누적자(스트림의 첫
     * n-1개 항목을 수집한 상태)와 n번째 요소를 함수에 적용한다.
     * 함수의 반환값은 void, 즉 요소를 탐색하면서 적용하는 함수에 의해
     * 누적자 내부상태가 바뀌므로 누적자가 어떤 값일지 단정할 수 없다.
     */
    @Override
    public BiConsumer<List<T>, T> accumulator() {
        return List::add;
    }

    /**
     * 두 결과 컨테이너 병합
     * 스트림의 서로 다른 서브파트를 병렬로 처리할 때 누적자가 이 결과를
     * 어떻게 처리할지 정의한다.
     * 
     * 스트림의 리듀싱을 병렬로 수행할 때 포크/조인 프레임워크와
     * Spliterator(7장 참고)를 사용한다.
     * 
     * 병렬 리듀싱 수행 과정
     * - 스트림을 분할해야 하는지 정의하는 조건이 거짓으로 바뀌기 전까지
     * 원래 스트림을 재귀적으로 분한한다.
     * (보통 분산된 작업의 크기가 너무 작아지면 병렬 수행의 효과가 상쇄된다.
     * 일반적으로 프로세싱 코어의 개수를 초과하는 병렬 작업은 비효율적이다.)
     * - 모든 서브스트림(substream)의 각 요소에 리듀싱 연산을 순차적으로
     * 적용해서 서브스트림을 병렬로 처리할 수 있다.
     * - combiner 메서드가 반환하는 함수로 모든 부분결과를 쌍으로 합친다.
     * 즉, 분할된 모든 서브스트림의 결과를 합치면서 연산이 완료된다.
     */
    @Override
    public BinaryOperator<List<T>> combiner() {
        return (list1, list2) -> {
            list1.addAll(list2);
            return list1;
        };
    }

    /**
     * 최종 변환값을 결과 컨테이너로 적용하기
     * 스트림 탐색을 끝내고 누적자 객체를 최종 결과로 변환하면서 누적 과정을
     * 끝낼 때 호출할 함수를 반환해야 한다.
     * 때로는 누적자 객체가 이미 최종 결과인 상황도 있다.
     * 이런 때는 변환 과정이 필요하지 않으므로 항등 함수를 반환한다.
     */
    @Override
    public Function<List<T>, List<T>> finisher() {
        return Function.identity();
    }

    /**
     * collect 메서드가 어떤 최적화(병렬화 같은)를 이용해서 리듀싱 연산을
     * 수행할 것인지 결정하도록 돕는 힌트 특성 집합을 제공한다.
     * 
     * Characteristics는 스트림을 병렬로 히듀스할 것인지 그리고 병렬로
     * 리듀스한다면 어떤 최적화를 선택해야 할지 힌트를 제공한다.
     * - UNORDERED: 리듀싱 결과는 스트림 요소의 방문 순서나 누적 순서에
     * 영향을 받지 않는다.
     * - CONCURRENT: 다중 스레드에서 accumulator 함수를 동시에 호출할
     * 수 있으며 이 컬렉터는 스트림의 병렬 리듀싱을 수행할 수 있다.
     * 미설정 시 데이터 소스가 정렬되어 있지 않는 상황에서만 병렬 리듀싱을
     * 수행할 수 있다.
     * - IDENTITY_FINISH: 리듀싱 과정의 최종 결과로 누적자 객체를 바로
     * 사용할 수 있다.
     * 또한 누적자 A를 결과 R로 안전하게 형변환할 수 있다.
     */
    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(
                Characteristics.IDENTITY_FINISH, Characteristics.CONCURRENT));
    }

    public static void test() {

        System.out.println("--- Test ToListCollector ---");
        /**
         * Collectors.toList 메서드가 반환하는 결과와 완전히 같은 것은 아니지만
         * 사소한 최적화를 제외하면 대체로 비슷하다.
         * 특히 자바 API에서 제공하는 컬렉터는 싱글턴 Collections.emptyList()로 빈 리스트를 반환한다.
         * 
         * 기존 코드의 toList는 팩토리지만 ToListCollector는 new로 인스턴스화한다는 점이 다르다.
         */
        System.out.printf("Dishes: %s\n", menu.stream()
                .collect(new ToListCollector<Dish>()));

        System.out.printf("Dishes: %s\n", menu.stream()
                .collect(toList()));

        /**
         * IDENTITY_FINISH 수집 연산에서는 Collector 인터페이스를
         * 완전히 새로 구현하지 않고도 같은 결과를 얻을 수 있다.
         * 
         * 이전 코드에 비해 좀 더 간결하고 축약되어 있지만 가독성은 떨어진다.
         * 적절한 클래스로 커스텀 컬렉터를 구현하는 편이 중복을 피하고 재사용성을 높이는 데 도움이 된다.
         * 
         * 또한 Characteristics를 전달할 수 없다.
         * 즉, IDENTITY_FINISH와 CONCURRENT지만 UNORDERED는 아닌 컬렉터로만 동작한다.
         */
        System.out.printf("Dishes: %s\n\n", menu.stream()
                // .collect(ArrayList::new, List::add, List::addAll));
                // for maven
                .collect(ArrayList<Dish>::new, (ArrayList<Dish> list, Dish dish) -> {
                    list.add(dish);
                }, (ArrayList<Dish> list1, ArrayList<Dish> list2) -> {
                    list1.addAll(list2);
                }));
    }
}

package chapter06;

import static chapter04.Dish.menu;
import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summarizingInt;
import static java.util.stream.Collectors.summingInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Stream;

import chapter04.Dish;

/**
 * 함수형 프로그래밍(특히 자바 8의 컬렉션 프레임워크에 추가된 함수형 원칙에 기반한
 * 새로운 API)에서는 하나으 연산을 다양한 방법으로 해결할 수 있음을 보여준다.
 * 또한 스트림 인터페이스에서 직접 제공하는 메서드를 이용하는 것에 비해 컬렉터를
 * 이용하는 코드가 더 복잡하다는 사실도 보여준다.
 * 코드가 좀 더 복잡한 대신 재사용성과 커스터마이즈 가능성을 제공하는 높은 수준의
 * 추상화와 일반화를 얻을 수 있다.
 * 문제를 해결할 수 있는 다양한 해결 방법을 확인한 다음에 가장 일반적으로 문제에
 * 특화된 해결책을 고르는 것이 바람직하다.
 */
public class Summarizing {

    public static void test() {
        // 리듀싱과 요약
        System.out.println("--- Test reducing and summerization ---");
        System.out.printf("How many dishes is there?: %d\n", menu.stream().collect(counting()));
        System.out.printf("How many dishes is there?: %d\n\n", menu.stream().count());

        Comparator<Dish> dishCaloriesComparator = Comparator.comparingInt(Dish::getCalories);
        menu.stream()
                .collect(maxBy(dishCaloriesComparator))
                .ifPresent(dish -> System.out.printf("The highest calories dish: %s(%dcal)\n\n",
                        dish.getName(),
                        dish.getCalories()));

        // 요약(summarization): 스트림에 있는 객체의 숫자 필드의 합계나
        // 평균 등을 반환하는 연산에도 리듀싱 기능이 자주 사용된다.

        /**
         * Collectors.summingInt는 객체를 int로 매핑하는 함수를 인수로 받는다.
         * summingInt의 인수로 전달된 함수는 객체를 int로 매핑한 컬렉터를 반환한다.
         * 그리고 summingInt가 collect 메서드로 전달되면 요약 작업을 수행한다.
         */
        System.out.printf("The total calories of menu: %dcal\n",
                menu.stream().collect(summingInt(Dish::getCalories)));
        System.out.printf("The average calories of menu: %.2fcal\n\n",
                menu.stream().collect(averagingInt(Dish::getCalories)));

        /**
         * 두 개 이상의 연산을 한 번에 수행해야 할 때도 있다.
         * 이런 상황에서는 summarizingInt가 반환하는 컬렉터를 사용할 수 있다.
         */
        IntSummaryStatistics menuStatistics = menu.stream()
                .collect(summarizingInt(Dish::getCalories));
        System.out.println(menuStatistics + "\n");

        /**
         * joining 팩토리 메서드를 이용하면 스트림의 각 객체에 toString 메서드를
         * 호출해서 추출한 모든 문자열을 하나의 문자열로 연결해서 반환한다.
         * 
         * 내부적으로 StringBuilder를 이용해서 문자열을 하나로 만든다.
         */
        System.out.printf("Short menu: %s\n\n", menu.stream()
                .map(Dish::getName)
                .collect(joining(", ")));

        /**
         * 지금까지 살펴본 모든 컬렉터는 reducing 팩토리 메서드로도 정의할 수 있다.
         * 즉, 범용 Collectors.reducing으로도 구현할 수 있다.
         */
        System.out.printf("The total calories of menu: %dcal\n", menu.stream()
                .collect(reducing(0, Dish::getCalories, (i, j) -> i + j)));

        menu.stream()
                .collect(reducing((dish1, dish2) -> dish1.getCalories() > dish2.getCalories() ? dish1 : dish2))
                .ifPresent(dish -> System.out.printf("The highest calories dish: %s(%dcal)\n",
                        dish.getName(), dish.getCalories()));

        System.out.printf("The total calrories of menu: %dcal\n", menu.stream()
                .collect(reducing(0, Dish::getCalories, Integer::sum)));

        /**
         * * collect와 reduce
         * 이들 메서드로 같은 기능을 구현할 수 있지만 의미론적인 문제와 실용성 문제 등
         * 두 가지 문제가 발생한다.
         * 
         * collect 메서드는 도출하려는 결과를 누적하는 컨테이너를 바꾸도록 설계된
         * 메서드인 반면, reduce는 두 값을 하나로 도출하는 불변형 연산이라는 점에서
         * 의미론적인 문제가 일어난다.
         * 아래 예제에서 reduce 메서드는 누적자로 사용된 리스트를 변환시키므로
         * reduce를 잘못 활용한 예에 해당된다.
         * 
         * 여러 스레드가 동시에 같은 데이터 구조체를 고치면 리스트 자체가 망가져버리므로
         * 리듀싱 연산을 병렬로 수행할 수 없다는 점도 문제다.
         * 이 문제를 해결하려면 매번 새로운 리스트를 할당해야 하고 따라서 객체 할당
         * 비용에 의해 성능이 저하될 것이다.
         * 가변 컨테이너 관련 작업이면서 병렬성을 확보하려면 collect 메서드로 리듀싱
         * 연산을 구현하는 것이 바람직하다.
         */
        Stream<Integer> numbersStream = Arrays.asList(1, 2, 3, 4, 5, 6).stream();
        numbersStream.reduce(
                new ArrayList<Integer>(),
                (List<Integer> list, Integer elem) -> {
                    list.add(elem);
                    return list;
                },
                (List<Integer> list1, List<Integer> list2) -> {
                    list1.addAll(list2);
                    return list1;
                })
                .forEach(System.out::println);
        System.out.println();
    }

    /**
     * counting 컬렉터도 reducing 팩토리 메서드를 이용해서 구현할 수 있다.
     * 
     * * 제네릭 와일드카드 '?' 사용법
     * '?'는 컬렉터의 누적자 형식이 알려지지 않았음을, 즉 누적자의 형식이 자유로움을
     * 의미한다.
     */
    public static <T> Collector<T, ?, Long> counting() {
        return reducing(0L, e -> 1L, Long::sum);
    }
}

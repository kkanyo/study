package chapter05;

import static chapter04.Dish.menu;

import java.util.Arrays;
import java.util.List;

/**
 * Interger 같은 결과가 나올 때까지 스트림의 모든 요소를 반복적으로 처리하는 질의를
 * 리듀싱 연산(모든 스트림 요소를 처리해서 값으로 도출하는)이라고 한다.
 * 
 * 함수형 프로그래밍 언어 용어로는 이 과정이 마치 종이(스트림)을 작은 조각이 될 때까지
 * 반복해서 접는 것과 비슷하다는 의미로 폴드(fold)라고 부른다.
 * 
 * 람다의 첫 번째 파라미터에 초깃값이 사용되었고, 스트림에서 요소를 소비하여 두 번째
 * 파라미터로 사용했다.
 * 연산의 결과가 새로운 누적값(accumulated value)이 되었고, 누적값으로 람다를 다시
 * 호출하며 다음 요소를 소비한다.
 */
public class Reducing {

    public static void test() {
        System.out.println("*** Test reducing ***");

        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(numbers.stream().reduce(0, (a, b) -> a + b));
        System.out.println();

        System.out.println(numbers.stream().reduce(1, (a, b) -> a * b));
        System.out.println();

        // 메서드 참조를 이용해서 코드를 좀 더 간결하게 만들 수 있다.
        System.out.println(numbers.stream().reduce(0, Integer::sum));
        System.out.println();

        // 초기값을 받지 않도록 오버로드된 reduce도 있다.
        // 스트림에 아무 요소도 없는 상황이라면 초기값이 없으므로 reduce는 합계를 반환할 수 없다.
        // 따라서 합계가 없음을 가리킬 수 있도록 Optional 객체로 감싼 결과를 반환한다.
        /* Optional<Integer> sum = */ numbers.stream().reduce(Integer::sum);

        // 최댓값과 최솟값
        numbers.stream().reduce(Integer::max).ifPresent(System.out::println);
        numbers.stream().reduce(Integer::min).ifPresent(System.out::println);
        System.out.println();

        // Quiz 5-3
        // map과 reduce를 연결하는 기법을 맵 리듀스(map-reduce) 패턴이라고 하며, 쉽게 병렬화하는
        // 특징 덕분에 구글이 웹 검색에 적용하면서 유명해졌다.
        System.out.println("--- Quiz 5-3 ---");
        menu.stream()
                .map(dish -> 1)
                .reduce(Integer::sum)
                .ifPresent(System.out::println);
        System.out.println();

        /**
         * reduce 메서드의 장점과 병렬화
         * 
         * reduce를 이용하면 내부 반복이 추상화되면서 내부 구현에서 병렬로 reduce를 실행할 수 있게 된다.
         * 반복적인 합계에서는 sum 변수를 공유해야 하므로 쉽게 병렬화하기 어렵다.
         * 
         * 이 작업을 병렬화하려면 입력을 분할하고, 분할된 입력을 더한 다음에, 더한 값을 합쳐야 한다.
         * 가변 누적자 패턴(mutable accumulator pattern)은 병렬화와 거리가 너무 먼 기법이다.
         * 
         * int sum = numbers.parallelStream().reduce(0, Integer::sum);
         * 
         * 위 코드를 병렬로 실행하려면 대가를 지불해야 한다.
         * 즉, reduce에 넘겨준 람다의 상태(인스턴스 변수 같은)가 바뀌지 말아야 하며, 연산이 어떤 순서로
         * 실행되더라도 결과가 바뀌지 않는 구조여야 한다.
         */

        /**
         * 스트림 연산: 상태 없음과 상태 있음
         * 
         * 스트림 연산들은 각각 다양한 연산을 수행한다.
         * 따라서 각각의 연산은 내부적인 상태를 고려해야 한다.
         * 
         * map, filter 등은 입력 스트림에서 각 요소를 받아 0 또는 결과를 출력 스트림으로 보낸다.
         * 따라서 사용자가 제공한 람다나 메서드 참조가 내부적인 가변 상태를 갖지 않는다는 가정하에
         * 이들은 보통 상태가 없는, 즉 내부 상태를 갖지 않는 연산(stateless operation)이다.
         * 
         * 하지만 reduce, sum, max 같은 연산은 결과를 누적할 내부 상태가 필요하다.
         * 스트림에서 처리하는 요소 수와 관계없이 내부 상태의 크기는 한정(bounded)되어 있다.
         * 
         * 반면 sorted나 distinct 같은 연산은 filter나 map처럼 스트림을 입력으로 받아 다른
         * 스트림을 출력하는 것처럼 보일 수 있다.
         * 하지만 filter나 map과는 다르다.
         * 스트림의 요소를 정렬하거나 중복을 제거하려면 과거의 이력을 알고 있어야 한다.
         * 예를 들어 어떤 요소를 출력 스트림으로 추가하려면 모든 요소가 버퍼에 추가되어 있어야 한다.
         * 연산을 수행하는 데 필요한 저장소 크기는 정해져있지 않다.
         * 따라서 데이터의 크기가 무한이라면 문제가 생길 수 있다.
         * 이러한 연산을 내부 상태를 갖는 연산(stateful operation)이라고 한다.
         */
    }
}

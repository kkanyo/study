package chapter05;

import static chapter04.Dish.menu;

import java.util.Arrays;
import java.util.List;

import chapter04.Dish;

/**
 * 스트림은 함수를 인수로 받는 map 메서드를 지원한다.
 * 인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다.
 * 이 과정은 기존의 값을 고친다(modify)라는 개념보다는 '새로운 버전을 만든다'라는
 * 개념에 가까우므로 변환(transforming)에 가까운 매핑(mapping)이라는 단어를 사용한다.
 */
public class Mapping {

    public static void test() {
        System.out.println("*** Test mapping ***");

        // List<String>
        menu.stream()
                .map(Dish::getName)
                .forEach(System.out::println);
        System.out.println();

        // List<Integer>
        List<String> words = Arrays.asList("Modern", "Java", "In", "Action");
        words.stream()
                .map(String::length)
                .forEach(System.out::println);
        System.out.println();

        menu.stream()
                .map(Dish::getName)
                .map(String::length)
                .forEach(System.out::println);
        System.out.println();

        // map으로 전달한 람다는 각 단어의 String[](문자열 배열)을 반환한다는 점이 문제다.
        // Stream<String[]>
        words.stream()
                .map(word -> word.split(""))
                .distinct()
                .forEach(word -> {
                    for (var ch : word) {
                        System.out.print(ch);
                    }
                    System.out.println();
                });
        System.out.println();

        // Arrays.stream: 문자열을 받아 스트림을 반환한다. (해결 X)
        words.stream()
                .map(word -> word.split(""))
                .map(Arrays::stream)
                .distinct()
                .forEach(word -> {
                    for (var ch : word.toArray()) {
                        System.out.print(ch);
                    }
                    System.out.println();
                });
        System.out.println();

        /**
         * flatMap
         * 각 배열을 스트림이 아니라 스트림의 콘텐츠로 매핑한다.
         * 즉, map(Arrays::stream)과 달리 flatMap은 하나의 평면화된 스트림을 반환한다.
         * 요약하면 flatMap메서드는 스트림의 각 값을 다른 스트림으로 만든 다음에
         * 모든 스트림을 하나의 스트림으로 연결하는 기능을 수행한다.
         */
        words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .forEach(System.out::println);
        System.out.println();

        // Quiz 5-2-1
        System.out.println("--- Quiz 5-2-1 ---");
        List<Integer> quizNum1 = Arrays.asList(1, 2, 3, 4, 5);
        quizNum1.stream()
                .map(n -> n *= n)
                .forEach(System.out::println);
        System.out.println();

        // Quiz 5-2-2
        System.out.println("--- Quiz 5-2-2 ---");
        List<Integer> quizNum2 = Arrays.asList(1, 2, 3);
        List<Integer> quizNum3 = Arrays.asList(3, 4);
        quizNum2.stream()
                .flatMap(num -> quizNum3.stream()
                        .map(num2 -> new int[] { num, num2 }))
                .forEach(arr -> {
                    for (var elem : arr) {
                        System.out.printf("%d ", elem);
                    }
                    System.out.println();
                });
        System.out.println();

        // Quiz 5-2-3
        System.out.println("--- Quiz 5-2-3 ---");
        quizNum2.stream()
                .flatMap(num -> quizNum3.stream()
                        // .filter(num2 -> (num + num2) % 3 == 0)) // 참고서 정답
                        .map(num2 -> new int[] { num, num2 }))
                .filter(arr -> {
                    int sum = 0;
                    for (var num : arr) {
                        sum += num;
                    }

                    return sum % 3 == 0 ? true : false;
                })
                .forEach(arr -> {
                    for (var elem : arr) {
                        System.out.printf("%d ", elem);
                    }
                    System.out.println();
                });
        System.out.println();
    }
}
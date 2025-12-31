package chapter06;

import static chapter04.Dish.menu;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.partitioningBy;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import chapter04.Dish;

/**
 * 분할은 분할 함수(partitioning function)라 불리는 프레디케이트를
 * 분류 함수로 사용하는 특수한 그룹화 기능이다.
 * 분할 함수는 불리언을 반환하므로 맵의 키 형식은 Boolean이다.
 * 
 */
public class Partitioning {

    public static void test() {
        System.out.println("--- Test partitioning ---");

        Map<Boolean, List<Dish>> partitionedMenu = menu.stream()
                .collect(partitioningBy(Dish::isVegetarian));
        System.out.printf("Vegetarian dishes: %s\n\n", partitionedMenu.get(true));

        /**
         * 분할 함수가 반환하는 참, 거짓 두 가지 요소의 스트림 시트를 모두
         * 유지한다는 것이 분할의 장점이다.
         * 
         * partitioningBy가 반환한 맵 구현은 참과 거짓 두 가지 키만
         * 포함하므로 더 간결하고 효과적이다.
         * 내부적으로 partitioningBY는 특수한 맵과 두 개의 필드로
         * 구현되어있다.
         */
        Map<Boolean, Map<Dish.Type, List<Dish>>> vegetarianDishesByType = menu.stream()
                .collect(
                        partitioningBy(Dish::isVegetarian,
                                groupingBy(Dish::getType)));
        System.out.printf("Vegerian dishes by type: %s\n\n", vegetarianDishesByType);

        System.out.printf("Partitioned prime numbers: %s\n\n", IntStream.rangeClosed(2, 100).boxed()
                .collect(
                        partitioningBy(candidate -> {
                            int candidateRoot = (int) Math.sqrt((double) candidate);
                            return IntStream.rangeClosed(2, candidateRoot)
                                    .noneMatch(i -> candidate % i == 0);
                        })));
    }
}
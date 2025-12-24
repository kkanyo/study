package chapter05;

import java.util.Arrays;
import java.util.List;

import chapter04.Dish;

import static chapter04.Dish.menu;

public class Filtering {
    public static void test() {
        // ** Filtering **
        System.out.println("Test filtering");

        // Predicate
        menu.stream()
                .filter(Dish::isVegetarian)
                .forEach(item -> System.out.printf("Is %s vegetarian? %b\n", item.getName(), item.isVegetarian()));
        System.out.println();

        // distinct: 중복을 필터링한다.
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
        numbers.stream()
                .filter(i -> i % 2 == 0)
                .distinct()
                .forEach(System.out::println);
        System.out.println();

        // ** Stream Slicing **
        System.out.println("Test stream slicing");

        List<Dish> specialMenu = Arrays.asList(
                new Dish("seasonal fruit", true, 120, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER));

        // Predicate
        specialMenu.stream()
                .filter(dish -> dish.getCalories() < 320)
                .forEach(dish -> System.out.printf("calories of %s are %d\n", dish.getName(), dish.getCalories()));
        System.out.println();

        /**
         * takeWhile
         * specialMenual는 이미 정렬되어 있다는 사실을 이용해 320 칼로리보다 크거나 같은 요리가 나왔을 때
         * 반복 작업을 중단할 수 있다.
         * 무한 스트림을 포함한 모든 스트림에 프레디케이트를 적용해 스트림을 슬라이스할 수 있다. (무한 루프에 빠지지 않도록 조심)
         */
        specialMenu.stream()
                .takeWhile(dish -> dish.getCalories() < 320)
                .forEach(dish -> System.out.printf("calories of %s are %d\n", dish.getName(), dish.getCalories()));
        System.out.println();

        /**
         * dropWhile
         * takeWhile과 정반대의 작업을 수행한다.
         * 프레디케이트가 처음으로 거짓이 되는 지점까지 발견된 요소를 버린다.
         * 프레디케이트가 거짓이 되면 그 지점에서 작업을 중단하고 남은 모든 요소를 반환한다.
         * 무한한 남은 요소를 가진 무한 스트림에서도 동작한다. (무한 루프에 빠지지 않도록 조심)
         */
        specialMenu.stream()
                .dropWhile(dish -> dish.getCalories() < 320)
                .forEach(dish -> System.out.printf("calories of %s are %d\n", dish.getName(), dish.getCalories()));
        System.out.println();

        // Stream.iterate(0, n -> n + 1)
        // .dropWhile(n -> n < 10)
        // .forEach(System.out::println);

        // limit: 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환한다.
        specialMenu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .limit(2)
                .forEach(dish -> System.out.printf("calories of %s are %d\n", dish.getName(), dish.getCalories()));
        System.out.println();

        // skip: 처음 n개 요소를 제외한 스트림을 반환한다.
        specialMenu.stream()
                .filter(dish -> dish.getCalories() > 300)
                .skip(2)
                .forEach(dish -> System.out.printf("calories of %s are %d\n", dish.getName(), dish.getCalories()));
        System.out.println();

        // Quiz 5-1
        menu.stream()
                .filter(dish -> dish.getType().equals(Dish.Type.MEAT))
                // .takeWhile(dish -> dish.getType().equals(Dish.Type.MEAT))
                .limit(2)

                .forEach(dish -> System.out.printf("type of %s is %s\n", dish.getName(), dish.getType()));

    }
}
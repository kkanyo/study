package com.practice.chapter06;

import static com.practice.chapter04.Dish.dishTags;
import static com.practice.chapter04.Dish.menu;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.practice.chapter04.Dish;

public class Grouping {

    public enum CaloricLevel {
        DIET, NORMAL, FAT
    }

    public static void test() {
        System.out.println("--- Test grouping ---");

        /**
         * 스트림의 각 요리에서 Dish.Type과 일치하는 모든 요리를 추출하는 함수를
         * groupingBy 메서드로 전달했다.
         * 이 함수를 기준으로 스트림이 그룹화되므로 이를 분류 함수(calssification
         * function)이라고 부른다.
         * 
         */
        Map<Dish.Type, List<Dish>> dishesByType = menu.stream()
                .collect(groupingBy(Dish::getType));
        System.out.printf("Dishes by type: %s\n\n", dishesByType);

        /**
         * 단순한 속성 접근자 대신 더 복잡한 분류 기준이 필요한 상황에서는 메서드
         * 참조를 분류 함수로 사용할 수 없다.
         * 따라서 메서드 참조 대신 람다 표현식으로 필요한 로직을 구현할 수 있다.
         */
        Map<CaloricLevel, List<Dish>> dishesByCaloricLevel = menu.stream()
                .collect(groupingBy(dish -> {
                    if (dish.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    } else {
                        return CaloricLevel.FAT;
                    }
                }));
        System.out.printf("Dishses by caloric level: %s\n\n", dishesByCaloricLevel);

        /**
         * 두 가지 기준으로 동시에 그룹화하는 방법
         * 
         * 그룹화를 하기 전에 프레디케이트로 필터를 적용해 문제를 해결할 수 있지만
         * 맵에 코드를 적용할 경우 프레디케이트를 만족하는 요소가 없을 경우 해당
         * 키 자체가 사라지는 문제가 있다.
         * 
         * Collectors 클래스는 일반적인 분류 함수에 Collector 형식의 두 번째
         * 인수를 갖도록 groupingBy 팩토리 메서드를 오버로드해 이 문제를 해결한다.
         * 
         * filtering 메소드는 Collectors 클래스의 또 다른 정적 팩토리 메서드로
         * 프레디케이트를 인수로 받는다.
         * 이 프레디케이트로 각 그룹의 요소와 필터링 된 요소를 재그룹화 한다.
         * 
         * 그룹화된 항목을 조작하는 다른 유용한 기능 중 또 다른 하나로 매핑 함수를
         * 이용해 요소를 변환하는 작업이 있다.
         * 매핑 함수와 각 항목에 적용한 함수를 모으는 데 사용하는 또 다른 컬렉터를
         * 인수로 받는 mapping 메서드를 제공한다.
         */
        Map<Dish.Type, List<Dish>> caloricDishesByType = menu.stream()
                .filter(dish -> dish.getCalories() > 500)
                .collect(groupingBy(Dish::getType));
        System.out.printf("Dishes by caloric and type: %s\n", caloricDishesByType);

        caloricDishesByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        filtering(dish -> dish.getCalories() > 500, toList())));
        System.out.printf("Dishes by caloric and type: %s\n\n", caloricDishesByType);

        Map<Dish.Type, List<String>> dishNamesByType = menu.stream()
                .collect(groupingBy(Dish::getType, mapping(Dish::getName, toList())));
        System.out.printf("Dishes name by type: %s\n\n", dishNamesByType);

        // gourpingBy와 연계해 세 번째 컬렉터를 사용해서 일반 맵이 아닌 flatMap
        // 변환을 수행할 수 있다.
        Map<Dish.Type, Set<String>> dishTagsByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        flatMapping(dish -> dishTags.get(dish.getName()).stream(), toSet())));
        System.out.printf("Dishes tags by type: %s\n\n", dishTagsByType);

        /**
         * 다수준 그룹화
         * Collectors.groupingBy는 일반적인 분류 함수와 컬렉터를 인수로 받는다.
         * 즉, 바깥쪽 groupingBy 메서드에 스트림의 항목을 분류할 두 번째 기준을
         * 정의하는 내부 groupingBy를 전달해서 두 수준으로 스트림의 항목을
         * 그룹화할 수 있다.
         * 
         * 보통 groupingBy의 연산을 '버킷(bucket)' 개념으로 생각하면 쉽다.
         * 첫 번째 groupingBy는 각 키의 버킷을 만든다.
         * 그리고 준비된 각각의 버킷을 서브스트림 컬렉터로 채워가기를 반복하면서
         * n수준 그룹화를 달성한다.
         */
        Map<Dish.Type, Map<CaloricLevel, List<Dish>>> dishesByTypeCaloricLevel = menu.stream().collect(
                groupingBy(Dish::getType,
                        groupingBy(dish -> {
                            if (dish.getCalories() <= 400) {
                                return CaloricLevel.DIET;
                            } else if (dish.getCalories() <= 700) {
                                return CaloricLevel.NORMAL;
                            } else {
                                return CaloricLevel.FAT;
                            }
                        })));
        System.out.printf("Dishes by type and caloric level: %s\n\n", dishesByTypeCaloricLevel);

        // 서브그룹으로 데이터 수집
        Map<Dish.Type, Long> typesCount = menu.stream()
                .collect(groupingBy(Dish::getType, counting()));
        System.out.printf("Count of dishes by type: %s\n\n", typesCount);

        // groupingBy 컬렉터는 스트림의 첫 번째 요소를 찾은 이후에야
        // 그룹화 맵에 새로운 키를 (Lazy하게) 추가한다.
        // 리듀싱 컬렉터는 절대 Optional.empty()를 반환하지 않는다.
        Map<Dish.Type, Optional<Dish>> mostCaloricByType = menu.stream()
                .collect(groupingBy(Dish::getType,
                        maxBy(comparingInt(Dish::getCalories))));
        System.out.printf("The highest calories dish by type: %s\n", mostCaloricByType);

        /**
         * Collectors.collectingAndThen은 적용할 컬렉터와 변환 함수를
         * 인수로 받아 다른 컬렉터를 반환한다.
         * 반환되는 컬렉터는 기존 컬렉터의 래퍼 역할을 하며 collect의 마지막
         * 과정에서 변환 함수로 자신이 반환하는 값을 매핑한다.
         */
        Map<Dish.Type, Dish> mostCaloricByType2 = menu.stream()
                .collect(groupingBy(Dish::getType,
                        collectingAndThen(maxBy(comparingInt(Dish::getCalories)),
                                Optional::get)));
        System.out.printf("The highest calories dish by type: %s\n\n", mostCaloricByType2);

        Map<Dish.Type, Integer> totalCaloriesByTypeMap = menu.stream()
                .collect(groupingBy(Dish::getType,
                        summingInt(Dish::getCalories)));
        System.out.printf("Sum of calrories by type: %s\n\n", totalCaloriesByTypeMap);

        Map<Dish.Type, Set<CaloricLevel>> caloricLevelsByType = menu.stream()
                .collect(groupingBy(Dish::getType, mapping(dish -> {
                    if (dish.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    } else {
                        return CaloricLevel.FAT;
                    }
                }, toSet())));
        System.out.printf("The caloric level of dish by type: %s\n", caloricLevelsByType);

        caloricLevelsByType = menu.stream()
                .collect(groupingBy(Dish::getType, mapping(dish -> {
                    if (dish.getCalories() <= 400) {
                        return CaloricLevel.DIET;
                    } else if (dish.getCalories() <= 700) {
                        return CaloricLevel.NORMAL;
                    } else {
                        return CaloricLevel.FAT;
                    }
                }, toCollection(HashSet::new))));
        System.out.printf("The caloric level of dish by type: %s\n\n", caloricLevelsByType);
    }

}

package com.practice.chapter05;

import static com.practice.chapter04.Dish.menu;

import java.util.Arrays;
import java.util.List;

import com.practice.chapter04.Dish;

public class SearchingMatching {

    public static void test() {
        System.out.println("*** Test searching and matching ***");

        // anyMatch: 프레디케이트가 주어진 스트림에서 적어도 한 요소와 일치하는지 확인한다.
        // boolean을 반환하므로 최종 연산이다.
        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }
        System.out.println();

        // allMatch: 스트림의 모든 요소가 주어진 프레디케이트와 일치하는지 검사한다.
        if (menu.stream().allMatch(dish -> dish.getCalories() < 1000)) {
            System.out.println("The menu is all healthy!!");
        }

        // noneMatch: 주어진 프레디케이트와 일치하는 요소가 없는지 확인한다. (allMatch와 반대)
        if (menu.stream().noneMatch(d -> d.getCalories() >= 1000)) {
            System.out.println("The menu is all healthy!!");
        }
        System.out.println();

        /**
         * anyMatch, allMatch, noneMatch 세 메서드는 스트림 쇼트서킷 기법,
         * 즉 자바의 &&, ||와 같은 연산을 활용한다.
         * 
         * * 쇼트서킷 평가 *
         * 표현식에서 하나라도 거짓이라는 결과가 나오면 나머지 표현식의 결과와 상관없이
         * 전체 결과도 거짓이 되는 상황을 쇼트서킷이라고 부른다.
         * 
         * allMatch, noneMatch, findFirst, findAny 등의 연산은 모든 스트림의 요소를
         * 처리하지 않고도 결과를 반환할 수 있다.
         * 원하는 요소를 찾았으면 즉시 결과를 반환할 수 있다.
         * 
         * 마찬가지로 스트림의 모든 요소를 처리할 필요 없이 주어진 크기의 스트림을 생성하는
         * limit도 쇼트 서킷 연산이다.
         */

        // *** 요소 검색 ***
        // findAny: 현재 스트림에서 임의의 요소를 반환한다.
        // 스트림 파이프라인은 내부적으로 단일 과정으로 실행할 수 있도록 최적화된다.
        // 즉, 쇼트서킬을 이용해서 결과를 찾는 즉시 실행을 종료한다.
        /* Optional<Dish> dish = */menu.stream()
                .filter(Dish::isVegetarian)
                .findAny()
                .ifPresent(dish -> System.out.println(dish.getName()));
        System.out.println();

        /**
         * Optional이란?
         * Optional<T> 클래스({@link java.util.Optional})는 갑의 존재나 부재 여부를
         * 표현하는 컨테이너 클래스이다.
         * null은 쉽게 에러를 일으킬 수 있으므로 만들어졌다.
         * 
         * Optional을 이용해서 null 확인 관련 버그를 피하는 방법 (10장 참고)
         * - isPresent()는 Optional이 값을 포함하면 true를 반환하고, 값을 포함하지 않으면
         * false는 반환한다.
         * - ifPresent(Consumer<T> block)은 값이 있으면 주어진 블록을 실행한다.
         * - T get()은 값이 존재하면 값을 반환하고, 값이 없으면 NoSuchElementException을
         * 일으킨다.
         * - T orElse(T other)은 값이 있으면 값을 반환하고, 값이 없으면 기본값을 반환한다.
         */

        // findFirst: 스트림에서 첫 번재 요소를 반환한다.
        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        someNumbers.stream()
                .map(n -> n * n)
                .filter(n -> n % 3 == 0)
                .findFirst()
                .ifPresent(System.out::println);
        System.out.println();

        /**
         * findFirst와 findAny는 언제 사용하는가
         * 병렬 실행에서는 첫 번째 요소를 찾기 어렵다.
         * 따라서 요소의 반환 순서가 상관없다면 병렬 스트림에서는 제약이 적은 findAny를 사용한다.
         */
    }
}
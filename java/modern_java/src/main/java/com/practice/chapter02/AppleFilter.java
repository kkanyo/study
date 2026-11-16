package com.practice.chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.practice.chapter02.Apple.Color;

public class AppleFilter {
    /*
     * 녹색 사과가 아닌 다른 색 사과를 필터링하고 싶을 때와 같은
     * 변화에 적절하게 대응할 수 없다.
     */
    public static List<Apple> filterGreenApples(List<Apple> inventory) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            result.add(apple);
        }

        return result;
    }

    /*
     * 각 사과에 필터링 조건을 적용하는 부분의 코드가 대부분 중복된다.
     * 이는 소프트웨어 공학의 DRY(don't repeat yourself) 원칙을 어기는 것이다.
     */
    public static List<Apple> filterAppleByColoer(List<Apple> inventory, Color color) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (apple.getColor().equals(color)) {
                result.add(apple);
            }
        }

        return result;
    }

    public static List<Apple> filterApplesByWieApples(List<Apple> inventory, int weight) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (apple.getWeight() > weight) {
                result.add(apple);
            }
        }

        return result;
    }

    /*
     * true와 false가 의미하는 바가 명확하지 않다.
     * 이후 요구사항이 바뀌었을 때 유연하게 대응할 수도 없다.
     */
    @Deprecated
    public static List<Apple> filterApples(List<Apple> inventory, Color color, int weight, boolean flag) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (flag && apple.getColor().equals(color) ||
                    !flag && apple.getWeight() > weight) {
                result.add(apple);
            }
        }

        return result;
    }

    /**
     * {@link com.practice.chapter02.ApplePredicate}로 이동
     */

    public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();

        for (Apple apple : inventory) {
            if (p.test(apple)) {
                result.add(apple);
            }
        }

        return result;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();

        for (T e : list) {
            if (p.test(e)) {
                result.add(e);
            }
        }

        return result;
    }
}

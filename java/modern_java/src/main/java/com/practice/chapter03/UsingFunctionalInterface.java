package com.practice.chapter03;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class UsingFunctionalInterface {
    /**
     * {@link java.util.function.Predicate} 인터페이스는 test라는 추상 메서드를 정의하며
     * test는 제너릭 형식 T의 객체를 인수로 받아 불리언을 반환한다.
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> results = new ArrayList<>();

        for (T t : list) {
            if (p.test(t)) {
                results.add(t);
            }
        }

        return results;
    }

    /**
     * {@link java.util.function.Consumer} 인터페이스는 제네릭 형식 T 객체를 받아서
     * void를 반환하는 accept라는 추상 메서드를 정의한다.
     * T 형식의 객체를 인수로 받아서 어떤 동작을 수행하고 싶을 때 Consumer 인터페이스를 사용할 수 있다.
     */
    public static <T> void forEach(List<T> list, Consumer<T> c) {
        for (T t : list) {
            c.accept(t);
        }
    }

    /**
     * {@link java.util.function.Function} 인터페이스는 제네릭 형식 T를 인수로 받아서
     * 제네릭 형식 R 객체를 반환하는 추상 메서드 apply를 정의한다.
     */
    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();

        for (T t : list) {
            result.add(f.apply(t));
        }

        return result;
    }

    /**
     * 자바의 모든 형식은 참조형(reference type), 예를 들면 Byte, Integer, Object, List
     * 아니면 기본형(primitive type), 예를 들면 int, double, byte, char)에 해당한다.
     * 하지만 제네릭 파라미터(예를 들면 Consumer<T>의 T)에는 참조형만 사용할 수 있다.
     * 
     * 자바에서는 기본형을 참조형으로 변환하는 기능을 제공하고 이 기능을 박싱(boxing)이라고 한다.
     * 참조형을 기본형으로 변환하는 반대 동작을 언박싱(unboxing)이라고 한다.
     * 프로그래머가 편리하게 코드를 구현할 수 있도록
     * 박싱과 언박싱이 자동으로 이루어지는 오토박싱(autoboxing)이라는 기능도 제공한다.
     * 
     * 박싱한 값은 기본형을 감싸는 래퍼며 힙(heap)에 저장된다.
     * 따라서 박싱한 값은 메모리를 더 소비하며 기본형을 가져올 때도 메모리를 탐색하는 과정이 필요하다.
     * 
     * 자바 8에서는 기본형을 입출력으로 사용하는 상황에서 오토박싱 동작을 피할 수 있도록 특별한 버전의
     * 함수형 인터페이스를 제공한다.
     */
}

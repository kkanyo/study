package com.practice.chapter07;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * {@link java.util.Spliterator}
 * Spliterator는 '분할할 수 있는 반복자(splitable iterator)'라는 의미다.
 * Iterator처럼 소스의 요소 탐색 기능을 제공한다는 점은 같지만 Spliterator는 병렬 작업에 특화되어 있다.
 * 자바 8은 컬렉션 프레임워크에 포함된 모든 자료구조에 사용할 수 있는 디폴트 Spliterator 구현을 제공한다.
 * 
 * `tryeAdvance` 메서드는 요소를 하나씩 순차적으로 소비하면서 탐색해야 할 요소가 남아있으면 참을 반환한다.
 * (즉, 일반적인 Iterator 동작과 같다.)
 * `trySplit` 메서드는 일부 요소(자신이 반환한 요소)를 분할해서 두 번째 Spliterator를 생성하는 메서드다.
 * `estimateSize` 메서드로 탐색해야 할 요소 수 정보를 제공할 수 있다.
 * 특히 제탐색해야 할 요소 수가 정확하진 안ㅀ더라도 제공된 값을 이용해서 더 쉽고 공평하게 Spliterator를 분할할 수 있다.
 * 
 * 재귀 분할 과정
 * 첫 번째 Spliterator에 trySplit을 호출하면 두 번째 Spliterator가 생성된다.
 * 이처럼 trySplit의 결과가 null이 될 때까지 이 과정을 반복한다.
 * trySplit이 null을 반환했다는 것은 더 이상 자료구조를 분할할 수 없음을 의미하며 재귀 분할 과정이 종료된다.
 */
public class WordCounterSpliterator implements Spliterator<Character> {

    private final String string;
    private int currentChar = 0;

    public WordCounterSpliterator(String string) {
        this.string = string;
    }

    /**
     * <? super Character>는 와일드카드(wildcard)를 사용한 하향 경계(lower bound) 지정으로, 제네릭 타입
     * 매개변수가 Character 타입이거나 Character의 상위 클래스(슈퍼 클래스)여야 함을 의미한다.
     * 
     * ? super T (Consumer Super): 컬렉션에 T 타입의 객체를 추가(소비)하려 할 때 사용한다.
     * T 및 T의 상위 타입 리스트를 혀용한다.
     * 
     * ? extends T (Producer Extends): 컬렉션에서 T 타입의 객체를 가져오기(생산)만 할 때 사용한다.
     * T 및 T의 하위 타입 리스트를 허용한다.
     */
    @Override
    public boolean tryAdvance(Consumer<? super Character> action) {
        action.accept(string.charAt(currentChar++));
        return currentChar < string.length();
    }

    @Override
    public Spliterator<Character> trySplit() {
        int currentSize = string.length() - currentChar;

        if (currentSize < 10) {
            return null;
        }

        // 파싱할 문자열의 중간을 분할 위치로 설정한다.
        for (int splitPos = currentSize / 2 + currentSize; splitPos < string.length(); splitPos++) {

            // 다음 공백이 나올 때까지 분할 위치를 뒤로 이동시킨다. 
            if (Character.isWhitespace(string.charAt(splitPos))) {
                Spliterator<Character> spliterator = new WordCounterSpliterator(
                        string.substring(currentChar, splitPos));
                currentChar = splitPos;

                return spliterator;
            }
        }

        return null;
    }

    @Override
    public long estimateSize() {
        return string.length() - currentChar;
    }

    @Override
    public int characteristics() {
        return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
    }

}

package com.practice.chapter08;

import static java.util.Map.entry;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionFactory {
    public static void main(String[] args) {
        /**
         * 리스트 팩토리
         * 
         * 변경할 수 없는 리스트로 컬렉션이 의도치 않게 변하는 것을 방지한다.
         * 하지만 요소 자체가 변하는 것을 막을 수 있는 방법은 없다.
         * null 요소는 금지하므로 의도치 않은 버그를 방지하고 조금 더 간결한 내부 구현을 달성했다.
         * 
         * 데이터 처리 형식을 설정하거나 데이터를 변환할 필요가 없다면 사용하기 간편한 팩토리 메서드를 이용할 것을 권장한다.
         * 팩토리 메서드 구현이 더 단순하고 목저을 달성하는데 충분하기 때문이다.
         */
        List<String> friends = List.of("Raphael", "Olivia", "Thibaut");
        System.out.println(friends + "\n");

        try {
            friends.add("Chih-Chun");
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }

        try {
            friends.set(0, "Vertin");
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        System.out.println();

        /**
         * 오버로딩 vs 가변 인수
         * 
         * List.of의 다양한 오버로드 버전이 있는데 왜 다중 요소를 받을 수 있도록 자바 API를 만들지 않은 것일까
         * 내부적으로 가변 인수 버전은 추가 배열을 할당해서 리스트로 감싼다.
         * 따라서 배열을 할당하고 초기화하며 나중에 가비지 컬렉션 비용을 지불해야 한다.
         * 고정된 숫자의 요소(최대 10개까지)를 API로 정의하므로 이런 비용을 제거할 수 있다.
         * List.of로 10개 이상의 요소를 가진 리스트를 만들 수도 있지만 이 때는 가변 인수를 이용하는 메소드가 사용된다.
         */

        // 집합 팩토리
        Set<String> setFriends = Set.of("Raphael", "Olivia", "Thibaud");
        System.out.println(setFriends + "\n");

        try {
            setFriends = Set.of("Raphael", "Olivia", "Olivia");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        System.out.println();

        /**
         * 맵 팩토리
         * 
         * 두 가지 방법으로 바꿀 수 없는 맵을 초기화할 수 있다.
         * 1. Map.of 팩토리 메서드에 키와 값을 번갈아 제공하는 방법으로 맵을 만들 수 있다.
         * 10개 이하의 키와 값 쌍을 가진 작은 맵을 만들 때는 이 메소드가 유용하다.
         * 
         * 그 이상의 맵에서는
         * 2. Map.Entry<K, V> 객체를 인수로 받으며 가변 인수로 구현된 Map.ofEntries 팩토리 메서드를 이용하는 것이 좋다.
         * 이 메서드는 키와 값을 감쌀 추가 객체 할당을 필요로 한다.
         * Map.entry는 Map.Entry 객체를 만드는 새로운 팩토리 메서드다.
         */
        Map<String, Integer> ageOfFriendsOf = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
        System.out.println(ageOfFriendsOf);

        Map<String, Integer> ageOfFriendsEntry = Map.ofEntries(
                entry("Raphael", 30),
                entry("Olivia", 25),
                entry("Thibaut", 26));
        System.out.println(ageOfFriendsEntry + "\n");
    }
}
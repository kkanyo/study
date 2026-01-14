package com.practice.chapter08;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link java.util.concurrent.ConcurrentHashMap} 클래스는 동시성 친화적이며 최신 기술을 반영한
 * HashMap 버전이다.
 * COncurrentHashMap은 내부 자료구조의 특정 부분만 잠궈 동시 추가, 갱신 작업을 허용한다.
 * 따라서 동기화된 Hashtable 버전에 비해 읽기 쓰기 연산 성능이 월등하다.
 * 표준 HashMap은 비동기로 동작한다.
 */
public class PracticeConcurrentHashMap {
    public static void main(String[] args) {
        /**
         * 리듀스와 검색
         * 
         * - forEach: 각 (키, 값) 상에 주어진 액션을 실행
         * - reduce: 모든 (키, 값) 쌍을 제공된 리듀스 함수를 이용해 결과로 합침
         * - search: 널이 아닌 값을 반환할 때까지 각 (키, 값) 쌍에 함수를 적용
         * 
         * - 키, 값으로 연산(forEach, reduce, search)
         * - 키로 연산(forEachKey, reduceKeys, searchKeys)
         * - 값으로 연산(forEachValue, reduceValues, searchValues)
         * - Map.Entry 객체로 연산(forEachEntry, reduceEntries, searchEntries)
         * 
         * 이들 연산은 ConcurrentHashMap의 상태를 잠그지 않고 연산을 수행한다는 점을 주목해야 한다.
         * 따라서 이들 연산에 제공한 함수는 계산이 진행되는 동안 바뀔 수 있는 객체, 값, 순서 등에 의존하지 않아야 한다.
         * 
         * 또한 이들 연산에 병렬성 기준값(threshold)을 지정해야 한다.
         * 맵의 크기가 주어진 기준값보다 작으면 순차적으로 연산을 실행한다.
         * 기준값을 1로 지정하면 공통 스레드 풀을 이용해서 병렬성을 극대화한다.
         * Long.MAX_VALUE를 기준값으로 설정하면 한 개의 스레드로 연산을 실행한다.
         */
        ConcurrentHashMap<String, Long> map = new ConcurrentHashMap<>();
        long parallelismThreshold = 1;

        @SuppressWarnings("unused")
        Optional<Long> maxValue = Optional.ofNullable(map.reduceValues(parallelismThreshold, Long::max));

        // 기본값에는 전용 each reduce 연산이 제공되므로 박싱 작업을 할 필요 없이 효율적으로 작업을 처리할 수 있다.
        maxValue = Optional.ofNullable(map.reduceValuesToLong(parallelismThreshold, i -> i, 0L, Long::max));

        /**
         * 계수
         * 
         * ConcurrentHashMap 클래스는 맵의 매핑 개수를 반환하는 mappingCount 메서드를 제공한다.
         * 기존의 size 메서드 대신 새 코드에서는 long을 반환하는 mappingCount 메서드를 사용하는 것이 좋다.
         * 그래야 매핑의 개수가 int의 범위를 넘어서는 이후의 상황을 대처할 수 있기 때문이다.
         */

        /**
         * 집합뷰
         * 
         * ConcurrentHashMap 클래스는 ConcurrentHashMap을 집합 뷰로 반환하는 keySet이라는 새 메서드를 제공한다.
         * 맵을 바꾸면 집합도 바뀌고 반대로 집합을 바꾸면 맵도 영향을 받는다.
         * newKeySet이라는 새 메서드를 이용해 ConcurrentHashMap으로 유지되는 집합을 만들 수도 있다.
         */
    }
}

package com.practice.chapter08;

import static java.util.Map.entry;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

public class ProcessMap {
        static MessageDigest messageDigest;

        public static void main(String[] args) throws NoSuchAlgorithmException {
                Map<String, Integer> ageOfFriendsOf = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);

                // forEach 메서드
                for (Map.Entry<String, Integer> entry : ageOfFriendsOf.entrySet()) {
                        String friend = entry.getKey();
                        Integer age = entry.getValue();

                        System.out.println(friend + " is " + age + " years old");
                }
                System.out.println();

                // Java 8부터 Map 인터페이스는 BiConsumer(키와 값을 인수로 받음)를 인수로 받은 forEach 메서드를 지원하므로 코드를
                // 조금 더 간단하게 구현할 수 있다.
                ageOfFriendsOf.forEach((friend, age) -> System.out.println(friend + " is " + age + " years old"));
                System.out.println("-----");

                // 정렬 메서드
                Map<String, String> favouriteMovies = Map.ofEntries(
                                entry("Raphael", "Star Wars"),
                                entry("Cristina", "Matrix"),
                                entry("Olivia", "James Bond"));

                /**
                 * forEach vs forEachOrdered
                 * 
                 * forEach는 순서를 보장하지 않고 병렬 처리 시 각 요소를 독립적으로 처리하므로 속도가 빠르다.
                 * 하지만 병렬 처리 시 출력 순서가 섞일 가능성이 있다.
                 * forEachOrdered는 순차 스트림처럼 원래의 데이터 순서를 유지하며 작업을 수행한다.
                 * 순서를 유지하기 위한 추가적인 오버헤드가 발생하여 성능이 저하될 수 있다.
                 */
                favouriteMovies.entrySet().stream()
                                .sorted(Entry.comparingByKey())
                                .forEachOrdered(System.out::println);
                System.out.println();

                favouriteMovies.entrySet().stream()
                                .sorted(Entry.comparingByValue())
                                .forEachOrdered(System.out::println);
                System.out.println("-----");

                /**
                 * HashMap 성능
                 * 
                 * 기존에 맵의 항목은 키로 생성한 해시코드로 접근할 수 있는 버켓에 저장했다.
                 * 많은 키가 같은 해시코드를 반환하는 상황이 되면 O(n)의 시간이 걸리는 LinkedList로 버킷을 반환해야 하므로 성능이 저하된다.
                 * 
                 * Java 8부터는 버킷이 너무 커질 경우 O(log(N))의 시간이 소요되는 정렬된 트리를 이용해 동적으로 치환해 충돌이 일어나는 요소
                 * 반환 성능을 개선했다.
                 * 하지만 키가 String, Number 클래스 같은 Comparable의 형태여야만 정렬된 트리가 지원된다.
                 */

                // getOrDefault 메서드
                System.out.println(favouriteMovies.getOrDefault("Vertin", "Matrix"));
                System.out.println(favouriteMovies.getOrDefault("Olivia", "Matrix"));
                System.out.println("-----");

                /**
                 * 계산 패턴
                 * 
                 * 맵에 키가 존재하는지 여부에 따라 어떤 동작을 싱행하고 결과를 저장해야 하는 상황이 필요한 떄가 있다.
                 * 예를 들어 키를 이용해 값비싼 동작을 실행해서 얻은 결과를 캐시하려고 한다.
                 * 키가 존재하면 결과를 다시 계산할 필요가 없다.
                 * - computeIfAbsent: 제공된 키에 해당하는 값이 없으면(값이 없거나 NULL), 키를 이용해 새 값을 계산하고 맵에 추가한다.
                 * - computeIfPresent: 제공된 키가 존재하면 새 값을 계산하고 맵에 추가한다.
                 * - compute: 제공된 키로 새 값을 계산하고 맵에 저장한다.
                 */
                List<String> lines = Arrays.asList(
                                " Nel   mezzo del cammin  di nostra  vita ",
                                "mi  ritrovai in una  selva oscura",
                                " che la  dritta via era   smarrita ");

                Map<String, byte[]> dataToHash = new HashMap<>();
                messageDigest = MessageDigest.getInstance("SHA-256");

                lines.forEach(line -> dataToHash.computeIfAbsent(line, // line은 맵에서 찾을 키
                                ProcessMap::calculateDigest)); // 키가 존재하지 않으면 동작을 실행
                dataToHash.forEach((key, value) -> System.out.println("[" + key + "]: " + value));
                System.out.println();

                // 여러 값을 저장하는 맵을 처리할 때도 위 패턴을 유용하게 활용할 수 있다.
                // Map<K, List<V>>에 요소를 추가하려면 항목이 초기화되어 있는지 확인해야 한다.
                Map<String, List<String>> friendsToMovies = new HashMap<>();

                String friend = "Raphael";
                List<String> movies = friendsToMovies.get(friend);
                if (movies == null) {
                        movies = new ArrayList<>();

                        friendsToMovies.put(friend, movies);
                }
                movies.add("Star Wars");
                friendsToMovies.forEach((key, value) -> System.out.printf("[%s]: %s\n", key, value));

                friendsToMovies.clear();

                // computeIfPresent 메서드는 현재 키와 관련된 값이 맵에 존재하며 NULL이 아닐 때만 새 값을 계산한다.
                // 값을 만드는 함수가 NULL을 반환하면 현재 매핑을 맵에서 제거한다.
                // 하지만 매핑을 제거할 때는 remove 메서드를 오버라이드하는 것이 더 적합하다.
                friendsToMovies.computeIfAbsent("Raphael", name -> new ArrayList<>()).add("Star Wars");
                friendsToMovies.forEach((key, value) -> System.out.printf("[%s]: %s\n", key, value));
                System.out.println("-----");

                // 삭제 패턴
                // Java 8부터는 키가 특정한 값과 연관되었을 때만 항목을 제거하는 오버로드 버전 메서드를 제공한다.
                Map<String, String> favouriteMovies2 = new HashMap<>();
                favouriteMovies2.put("Raphael", "Star Wars");

                String key = "Raphael";
                String value = "Jack Reacher 2";
                if (favouriteMovies2.containsKey(key) && Objects.equals(favouriteMovies2.get(key), value)) {
                        favouriteMovies2.remove(key);
                }

                favouriteMovies2.remove(key, value);
                System.out.println(favouriteMovies2);
                System.out.println("-----");

                /**
                 * 교체 패턴
                 * 
                 * 맵의 항목을 바꾸는 데 사용할 수 있는 두 개의 메서드가 존재한다.
                 * - replaceAll: BiFunction을 적용한 결과로 각 항목의 값을 교체한다.
                 * 이 메서드는 이전에 살펴본 List의 replaceAll과 비슷한 동작을 수행한다.
                 * - replace: 키가 존재하면 맵의 값을 바꾼다.
                 * 키가 특정 값으로 매핑되었을 때만 값을 교페하는 오버로드 버전도 있다.
                 */
                Map<String, String> favouriteMovies3 = new HashMap<>();
                favouriteMovies3.put("Raphael", "Star Wars");
                favouriteMovies3.put("Olivia", "James Bond");

                favouriteMovies3.replaceAll((key3, value3) -> value3.toUpperCase());
                System.out.println(favouriteMovies3);
                System.out.println("-----");

                // merge 메서드
                // 두 개의 맵에서 값을 합치거나 바꿀 때 사용한다.
                Map<String, String> family = Map.ofEntries(
                                entry("Teo", "Star Wars"),
                                entry("Cristina", "James Bond"));
                Map<String, String> friends = Map.ofEntries(
                                entry("Raphael", "Star Wars"));
                Map<String, String> everyone = new HashMap<>(family);

                // friend의 모든 항목을 everyone으로 복사
                // 중복된 키가 없다면 아래 코드는 잘 동작한다.
                everyone.putAll(friends);
                System.out.println(everyone);

                /**
                 * 값을 좀 더 유연하게 합쳐야 한다면 새로운 merge 메서드를 이용할 수 있다.
                 * 이 메서드는 중복된 키를 어떻게 합칠지 결정하는 BiFunction을 인수로 받는다.
                 * 
                 * 지정된 키와 연관된 값이 없거나 NULL이면 merge 메서드는 키를 NULL이 아닌 값과 연결한다.
                 * 아니면 merge 메서드는 연결된 값을 주어진 매핑 함수의 결과 값으로 대치하거나 결과가 NULL이면 항목을 제거한다.
                 */
                family = Map.ofEntries(
                                entry("Teo", "Star Wars"),
                                entry("Cristina", "James Bond"));
                friends = Map.ofEntries(
                                entry("Raphael", "Star Wars"),
                                entry("Cristina", "Matrix"));

                everyone.clear();
                everyone.putAll(family);
                friends.forEach((k, v) -> everyone.merge(k, v, (movie1, movie2) -> movie1 + " & " + movie2));
                System.out.println(everyone + "\n");

                Map<String, Integer> counts = new HashMap<>();
                // 1. 키가 없는 경우: 1이 저장됨
                counts.merge("apple", 1, Integer::sum);
                System.out.println(counts);
                // 2. 키가 있는 경우: 기존 값(1) + 새 값(1) = 2로 업데이트
                counts.merge("apple", 1, Integer::sum);
                System.out.println(counts);
                // 3. 매핑 함수 결과가 NULL인 경우: 키 삭제
                counts.merge("apple", 1, (oldV, newV) -> null);
                System.out.println(counts + "\n");

                // merge를 이용해 초기화 검사를 구현할 수도 있다.
                Map<String, Long> moviesToCount = new HashMap<>();
                String movieName = "JamesBond";
                Long count = moviesToCount.get(movieName);
                if (count == null) {
                        moviesToCount.put(movieName, 1L);
                } else {
                        moviesToCount.put(movieName, count + 1L);
                }
                System.out.println(moviesToCount);

                // 키의 반환값이 NULL이므로 처음에는 1L이 사용된다.
                // 그 다음부터는 값이 1로 초기화되어 있으므로 BiFucntion을 적용해 값이 증가된다.
                moviesToCount.clear();
                moviesToCount.merge(movieName, 1L, (count2, increment) -> count2 + 1L);
                System.out.println(moviesToCount);
                System.out.println("-----");

                // Quiz 8-2
                Map<String, Integer> movies2 = new HashMap<>();
                movies2.put("JamesBond", 20);
                movies2.put("Matrix", 15);
                movies2.put("Harry Potter", 5);

                movies2.entrySet().removeIf(entry -> entry.getValue() < 10);
        }

        private static byte[] calculateDigest(String key) {
                return messageDigest.digest(key.getBytes(StandardCharsets.UTF_8));
        }
}

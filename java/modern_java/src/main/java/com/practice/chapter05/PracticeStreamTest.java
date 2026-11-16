package com.practice.chapter05;

import static com.practice.chapter04.Dish.menu;
import static java.util.Comparator.comparing;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.practice.chapter04.Dish;

public class PracticeStreamTest {
        public static void main(String[] args) {
                Filtering.test();

                Mapping.test();

                SearchingMatching.test();

                Reducing.test();

                // Practice
                Trader raoul = new Trader("Raoul", "Cambridge");
                Trader mario = new Trader("Mario", "Milan");
                Trader alan = new Trader("Alan", "Cambridge");
                Trader brian = new Trader("Brian", "Cambridge");

                List<Transaction> transaction = Arrays.asList(
                                new Transaction(brian, 2011, 300),
                                new Transaction(raoul, 2012, 300),
                                new Transaction(raoul, 2011, 400),
                                new Transaction(mario, 2012, 710),
                                new Transaction(mario, 2012, 700),
                                new Transaction(alan, 2012, 950));

                // 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
                transaction.stream()
                                .filter(tr -> tr.getYear() == 2011)
                                .sorted(comparing(Transaction::getValue))
                                .forEach(tr -> System.out.println(tr.toString()));
                System.out.println();

                // 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
                transaction.stream()
                                .map(tr -> tr.getTrader().getCity())
                                .distinct()
                                .forEach(System.out::println);
                System.out.println();

                // 3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
                transaction.stream()
                                .map(tr -> tr.getTrader())
                                .filter(tr -> tr.getCity().equals("Cambridge"))
                                .sorted(comparing(Trader::getName))
                                .forEach(tr -> System.out.println(tr.toString()));
                System.out.println();

                // 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
                transaction.stream()
                                .map(tr -> tr.getTrader().getName())
                                .distinct()
                                .sorted()
                                .forEach(System.out::println);
                System.out.println();

                // 5. 밀라노에 거래자가 있는가?
                if (transaction.stream().anyMatch(tr -> tr.getTrader().getCity().equals("Milano"))) {
                        System.out.println("True");
                } else {
                        System.out.println("False");
                }
                System.out.println();

                // 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
                transaction.stream()
                                .filter(tr -> tr.getTrader().getCity().equals("Cambridge"))
                                .forEach(tr -> System.out.println(tr.toString()));
                System.out.println();

                // 7. 전체 트랜잭션 중 최댓값은 얼마인가?
                transaction.stream()
                                .max(comparing(Transaction::getValue))
                                .ifPresent(System.out::println);
                System.out.println();

                // 8. 전체 트랜잭션 중 최솟값은 얼마인가?
                transaction.stream()
                                .map(tr -> tr.getValue())
                                .reduce(Integer::min)
                                .ifPresent(System.out::println);
                System.out.println();

                // 숫자형 스트림
                System.out.println("*** Test numeric stream ***");

                // 내부적으로 합계를 계산하기 전에 Integer를 기본형으로 언박싱해야 한다.
                // 또한 map 메서드가 Stream<T>를 생성하기 때문에 sum 메서드를 직접 호출할 수 없다.
                System.out.printf("Sum of calories: %d\n", menu.stream()
                                .map(Dish::getCalories)
                                .reduce(0, Integer::sum));

                /**
                 * 스트림 API 숫자 스트림을 효율적으로 처리할 수 있도록 기본형 특화 스트림(
                 * primitive stream specialization)을 제공한다.
                 * 
                 * 스트림 API는 박싱 비용을 피할 수 있도록 int 요소에 특화된 IntStream과
                 * 같은 인터페이스를 제공한다.
                 * 각각의 인터페이스는 숫자 관련 리듀싱 연산 수행 메서드(sum, max 등)를
                 * 제공한다.
                 * 또한 필요할 때 다시 객체 스트림으로 복원하는 기능도 제공한다.
                 */

                // 숫자 스트림으로 매핑
                System.out.printf("Sum of calories: %d\n\n", menu.stream() // Stream<Dish> 반환
                                .mapToInt(Dish::getCalories) // IntStream 반환
                                .sum()); // 스트림이 비어있으면 기본값 0을 반환한다.

                // 객체 스트림으로 복원하기
                IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
                Stream<Integer> stream = intStream.boxed();
                stream.close();

                // 스트림에 요소가 없는 상황과 실제 최댓값이 0인 상황을 어떻게 구별할 수 있을까?
                // OptionalInt를 이용해서 최댓값이 없는 상황에 사용할 기본값을 명시적으로 정의할 수 있다.
                System.out.printf("Max calories: %d\n\n", menu.stream()
                                .mapToInt(Dish::getCalories)
                                .max()
                                .orElse(1));

                /**
                 * 숫자 범위
                 * range 메서드는 시작값과 종료값이 결과에 포함되지 않는 반면
                 * rangeClosed는 시작값과 종료값이 결과에 포함된다는 점이 다르다.
                 */
                IntStream evenNumbers = IntStream.rangeClosed(1, 100)
                                .filter(n -> n % 2 == 0);
                System.out.printf("Count of even numbers 1 to 100: %d\n\n", evenNumbers.count());

                Stream<int[]> pythahoreanTriples = IntStream.rangeClosed(1, 100)
                                .boxed()
                                .flatMap(a -> IntStream.rangeClosed(a, 100)
                                                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                                                .mapToObj(b -> new int[] { a, b, (int) Math.sqrt(a * a + b * b) }));

                System.out.println("pythagoreanTriples: ");
                pythahoreanTriples.limit(5)
                                .forEach(t -> System.out.printf("%d, %d, %d\n", t[0], t[1], t[2]));
                System.out.println();

                // 스트림 만들기
                System.out.println("*** Test making stream ***");

                // 임의의 수를 인수로 받는 정적 메서드 Stream.of를 이용해서 스트림을 만들 수 있다.
                Stream<String> stringStream = Stream.of("Modern ", "Java ", "In ", "Action");
                stringStream.map(String::toUpperCase)
                                .forEach(System.out::println);
                System.out.println();

                stringStream = Stream.empty();

                // null이 될 수 있는 객체로 스트림 만들기
                // 때로는 null이 될 수 있는 객체를 스트림(객체가 null이라면 빈 스트림)으로 만들어야 할 수 있다.
                // 예를 들어 System.getProperty는 제공된 키에 대응하는 속성이 없으면 null을 반환한다.
                // 이런 메소드를 스트림에 활용하려면 null을 명시적으로 확인해야 한다.
                String homeValue = System.getProperty("home");
                Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(homeValue);
                System.out.println(homeValueStream.count() + "\n");

                // Stream.ofNullable을 이용해 구현할 수 있다.
                homeValueStream = Stream.ofNullable(System.getProperty("home"));

                Stream.of("config", "home", "user")
                                .flatMap(key -> Stream.ofNullable(System.getProperty(key)));

                // 배열로 스트림 만들기
                int[] numbers = { 2, 3, 5, 7, 11, 13 };
                System.out.printf("Sum of numbers: %d\n\n", Arrays.stream(numbers).sum());

                /**
                 * 파일로 스트림 만들기
                 * 파일을 처리하는 등의 I/O 연산에 사용하는 자바의 NIO API(비블록 I/O)도 스트림 API를 활용할
                 * 있도록 업데이트되었다.
                 * 
                 * {@link java.nio.file.Files}의 많은 정적 메서드가 스트림을 반환한다.
                 */

                /**
                 * Files.lines로 파일의 각 행 요소를 반환하는 스트림을 얻을 수 있다.
                 * 스트림의 소스가 I/O 자원이므로 이 메소드를 try/catch 블록으로 감쌌고
                 * 메모리 누수를 막으려면 자원을 닫아야 한다.
                 * Stream 인터페이스는 AutoCloseable 인터페이스를 구현한다.
                 * 따라서 try 블록 내의 자원은 자동으로 관리된다.
                 */
                try (Stream<String> lines = Files.lines(Paths.get("resources/data.txt"),
                                Charset.defaultCharset())) {
                        System.out.printf("Count words of data.txt: %d\n\n",
                                        lines.flatMap(line -> Arrays.stream(line.split(" ")))
                                                        .distinct()
                                                        .count());
                } catch (IOException e) {

                }

                /**
                 * 함수로 무한 스트림 만들기
                 * Stream.iterate와 Stream.generate를 이용해서 무한 스트림(infinite stream),
                 * 즉 크기가 고정되지 않은 스트림을 만들 수 있다.
                 * 보통 무한한 값을 출력하지 않도록 limit 연산을 함께 연결해서 사용한다.
                 */

                /**
                 * iterate 메서드
                 * 요청할 때마다 값을 생산할 수 있으며 끝이 없으므로 무한 스트림을 만들다.
                 * 이러한 스트림을 언바운드 스트림(unbounded stream)이라고 표현한다.
                 * 일반적으로 연속된 일련의 값을 만들 때는 iterate를 사용한다.
                 */
                Stream.iterate(0, n -> n + 2)
                                .limit(10)
                                .forEach(System.out::println);
                System.out.println();

                // Quiz 5-4
                System.out.println("--- Quiz 5-4 ---");
                Stream.iterate(new int[] { 0, 1 }, arr -> new int[] { arr[1], arr[0] + arr[1] })
                                .limit(20)
                                .forEach(t -> System.out.println("(" + t[0] + ", " + t[1] + ")"));
                System.out.println();

                // iterate 메소드는 프레디케이트를 지원한다.
                IntStream.iterate(0, n -> n < 100, n -> n + 4)
                                .forEach(System.out::println);
                System.out.println();

                // filter 메소드는 언제 이 작업을 중단해야 하는지를 알 수 없다.
                // 쇼트서킷을 지원하는 takeWhile을 이용하는 것이 옳바르다.
                IntStream.iterate(0, n -> n + 4)
                                // .filter(n -> n < 100)
                                .takeWhile(n -> n < 100)
                                .forEach(System.out::println);
                System.out.println();

                /**
                 * generate 메서드
                 * iterate와 달리 생성된 각 값을 연속적으로 계산하지 않는다.
                 * Supplier<T>를 인수로 받아서 새로운 값을 생산한다.
                 * 
                 * 아래 코드에서 사용한 발행자(supplier)는 상태가 없는 메서드,
                 * 즉 나중에 계산에 사용할 어떤 값도 저장해두지 않는다.
                 * 하지만 발행자에 꼭 상태가 없어야 하는 것은 아니다.
                 * 발행자가 상태를 저장한 다음에 스트림의 다음 값을 만들 때 상태를 고칠 수도 있다.
                 * 
                 * 여기서 중요한 점은 병렬 코드에서는 발행자에 상태가 있으면 안전하지 않다는 것이다.
                 * 따라서 상태를 갖는 발행자는 실제로는 피해야 한다.
                 */
                Stream.generate(Math::random)
                                .limit(5)
                                .forEach(System.out::println);
                System.out.println();

                /**
                 * 여기서 사용한 익명 클래스와 람다는 비슷한 연산을 수행하지만 익명 클래스에서는
                 * getAsInt 메서드의 연산을 커스터마이즈할 수 있는 상태 필드를 정의할 수 있다는
                 * 점이 다르다.
                 * 지금까지 살펴본 람다는 부작용이 없었다.
                 * 즉, 람다는 상태를 바꾸지 않는다.
                 */
                IntStream.generate(() -> 1);

                IntStream.generate(new IntSupplier() {
                        public int getAsInt() {
                                return 2;
                        }
                });

                /**
                 * generate를 이용한 피보나치수열은 기존의 수열 상태를 저장하고 getAsInt로 다음 요소를
                 * 계산하도록 IntSupplier를 만들어야 한다.
                 * 또한 다음에 호출될 때는 IntSupplier의 상태를 갱신할 수 있어야 한다.
                 * 
                 * IntSupplier 인스턴스는 기존 피보나치 요소와 두 인스턴스 변수에 어떤 피보나치 요소가
                 * 들어있는지 추적하므로 가변(mutable) 상태 객체다.
                 * getAsInt를 호출하면 객체 상태가 바뀌며 새로운 값을 생산한다.
                 * iterate를 사용했을 때는 각 과정에서 새로운 값을 생성하면서도 기존 상태를 바꾸지
                 * 않는 순수한 불변(immutable) 상태를 유지했다.
                 * 
                 * 스트림을 병렬로 처리하면서 올바른 결과를 얻으려면 불변 상태 기법을 고수애햐 한다. (7장 참고)
                 */
                IntSupplier fib = new IntSupplier() {
                        private int previous = 0; // 호출될 때마다 상태가 바뀜
                        private int current = 1; // 호출될 때마다 상태가 바뀜

                        public int getAsInt() {
                                int oldPrevious = this.previous;
                                int nextValue = this.previous + this.current;
                                this.previous = this.current;
                                this.current = nextValue;

                                return oldPrevious;
                        }
                };
                IntStream.generate(fib)
                                .limit(10)
                                .forEach(System.out::println);
                System.out.println();
        }
}

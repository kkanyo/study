package chapter03;

import static java.util.Comparator.comparing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

import chapter02.Apple;
import chapter02.Apple.Color;
import chapter02.AppleFormatter;
import chapter02.AppleSimpleFormatter;

/**
 * 람다 표현식은 메서드로 전달할 수 있는 익명 함수를 단순화한 것이라고 할 수 있다.
 * 
 * 람다 표현식의 특징
 * 1. 익명: 보통의 메서드와 달리 이름이 없으므로 익명이라 표현한다.
 * 2. 함수: 람다는 메서드처럼 특정 클래스에 종속되지 않는다.
 * 메서드처럼 파라미터 리스트, 바디, 반환 형식, 가능한 예외 리스트를 포함한다.
 * 3. 전달: 람다 표현식을 메서드 인수로 전달하거나 변수로 저장할 수 있다.
 * 4. 간결성
 * 
 * 람다 표현식에는 return이 함축되어 있으므로 return 문을 명시적으로 사용하지 않아도 된다.
 * 표현식 스타일(expression style): (parameters) -> expression
 * 블록 스타일(block-style): (parameters) -> { statements; }
 * 
 * 
 * 함수형 인터페이스는 정확히 하나의 추상 메서드를 지정하는 인터페이스다. (ex. Comparator, Runnable)
 * 인터페이스는 디폴트 메서드(인터페이스의 메서드를 구현하지 않은 클래스를 고려해서
 * 기본 구현을 제공하는 바디를 포함하는 메소드)를 포함할 수 없다.
 * 많은 디폴트 메서드가 있더라도 `추상 메서드가 오직 하나면` 함수형 인터페이스다.
 * 
 * 람다 표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있으므로
 * 전체 표현식을 함수형 인터페이스의 인스턴스로 취급할 수 있다.
 * (기술적으로 따지면 함수형 인터페이스를 구현한 클래스의 인스턴스)
 * 
 * 
 * 함수형 인터페이스의 추상 메서드 시그니처(signature, 컴파일러나 인터프리터가 함수를 서로 구별하기 위해
 * 사용하는 함수의 이름과 파타미터 목록 등 고유한 식별 정보)는 람다 표현식의 시그니처를 가리킨다.
 * 람다 표현식의 시그니처를 서술하는 메서드를 함수 디스크립터(function desciptor)라고 부른다.
 * 
 * {@link @FunctionalInterface}
 * 함수형 인터페이스임을 가리키는 어노테이션이다.
 * 인터페이스를 선언했지만 실제로 함수형 인터페이스가 아니면 컴파일러가 에러를 발생시킨다.
 * 
 * 
 * 람다가 사용되는 콘텍스트(context)를 이용해서 람다의 형식(type)을 추론할 수 있다.
 * 콘텍스트(람다가 전달될 메서드 파라미터나 람다가 할당되는 변수 등)에서 기대되는 람다 표현식의 형식을
 * 대상 형식(target type)이라고 부른다.
 * 
 * 대상 형식이라는 특징 때문에 같은 람다 표현식이더라도 호환되는 추상 메서드를 가진 다른 함수형 인터페이스로
 * 사용될 수 있다.
 */
public class LambdaTest {
    @SuppressWarnings("unused")
    public static void main(String[] args) throws IOException {
        /**
         * 중괄호는 필요 없다.
         * 자바 언어 명세에서는 void를 반환하는 메소드 호출과 관련한 특별한 규칙을 정하고 있기 때문이다.
         * 즉 한 개의 void 메소드 호출은 중괄호로 감쌀 필요가 없다.
         */
        process(() -> System.out.println("This is awesome!!\n"));

        // Execute arround pattern
        System.out.println("Execute arround pattern");
        String oueLine = processFile((BufferedReader br) -> br.readLine());
        System.out.printf("oueLine: %s\n", oueLine);

        String twoLine = processFile((BufferedReader br) -> br.readLine() + br.readLine());
        System.out.printf("twoLine: %s\n\n", twoLine);

        // Predicate
        System.out.println("Predicate Test");
        Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
        List<String> nonEmpty = UsingFunctionalInterface.filter(Arrays.asList("", "apple", "", "orange"),
                nonEmptyStringPredicate);
        System.out.println(nonEmpty + "\n");

        // Consumer
        System.out.println("Consumer Test");
        UsingFunctionalInterface.forEach(Arrays.asList(1, 2, 3, 4, 5), (Integer i) -> System.out.println(i));
        System.out.println();

        // Function
        System.out.println("Function Test");
        List<Integer> l = UsingFunctionalInterface.map(Arrays.asList("lambdas", "in", "action"),
                (String s) -> s.length());
        System.out.println(l + "\n");

        // Boxing and Unboxing
        IntPredicate evenNumbers = (int i) -> i % 2 == 0;
        evenNumbers.test(1000); // no boxing

        Predicate<Integer> oddNnumbers = (Integer i) -> i % 2 != 0;
        oddNnumbers.test(1000); // boxing

        /**
         * 람다의 바디에 일반 표현식이 있으면 void를 반환하는 함수 디스크립터와 호횐된다.
         * (파라미터 리스트도 호환되어야 한다.)
         * Consumer 콘텍스트(T -> void)가 기대하는 void 대신 boolean을 반환하지만 유효한 코드다.
         */
        List<String> list = new ArrayList<>();
        Consumer<String> b = s -> list.add(s);

        /**
         * 같은 함수형 디스크립터를 가진 두 함수형 인터페이스를 갖는 메소드를 오버로딩할 때
         * 이와 같은 기법을 활용할 수 있다.
         * 어떤 메소드의 시그니처가 사용되어야 하는지를 명식으로 구분하도록 람다를 캐스트할 수 있다.
         */
        Object o = (Runnable) () -> {
            System.out.println("Tricky example");
        };

        /**
         * 자바 컴파일러는 람다 표현식이 사용된 콘텍스트(대상 형식)을 이용해서 람다 표현식과 함수형 인터페이스를 추론한다.
         * 즉, 대상 형식을 이용해서 함수 디스크립터를 알 수 있으므로 컴파일러는 람다의 시그니처도 추론할 수 있다.
         * 결과적으로 컴파일러는 람다 표현식의 파라미터 형식에 접근할 수 있으므로 람다 문법에서 이를 생략할 수 있다.
         */
        Comparator<Apple> c1 = (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()); // 형식을 추론하지 않음
        Comparator<Apple> c2 = (a1, a2) -> a1.getWeight().compareTo(a2.getWeight()); // 형식을 추론함

        /**
         * 람다 표현식에서는 익명 함수가 하는 것처럼 자유 변수(free variable, 파라미터로 넘겨진 변수가 아닌
         * 외부에서 정의된 변수)를 활용할 수 있다.
         * 이와 같은 동작을 람다 캡처링(capturing lambda)이라고 부른다.
         * 
         * 람다는 인스턴스 변수와 정적 변수를 자유롭게 캡처(자신의 바디에서 참조할 수 있도록)할 수 있다.
         * 하지만 지역 변수는 명시적으로 final로 선언되어 있어야 하거나
         * 실질적으로 final로 선언된 변수와 똑같이 사용되어야 한다.
         * (참고: 인스턴스 변수 캡퍼는 final 지역 변수 this를 캡처하는 것과 마찬가지다.)
         * 
         * * 지역 변수의 제약
         * 인스턴스 변수는 힙에 저장되는 반면 지역 변수는 스택에 위치한다.
         * 람다가 스레드에서 실행된다면 변수를 할당한 스레드가 사라져서 변수 할당이 해제되었는데도
         * 람다를 실행하는 스레드에서는 해당 변수에 접근하려 할 수 있다.
         * 따라서 자바 구현에서는 원래 변수에 접근을 허용하는 것이 아니라 자유 지역 변수의 복사본을 제공한다.
         * 따라서 복사본의 값이 바뀌지 않아야 하므로 지역 변수에는 한 번만 값을 할당해야 한다는 제약이 생긴 것이다.
         * 
         * 클로저(closure)란 함수의 비지역 변수를 자유롭게 참조할 수 있는 함수의 인스턴스를 가리킨다.
         * 클로저는 클로저 외부에 정의된 변수의 값에 접근하고, 값을 바꿀 수 있다.
         * 다만 람다와 익명 클래스는 람다가 정의된 메소드의 지역 변수의 값은 바꿀 수 없다.
         * 람다가 정의된 메서드의 지역 변숫값은 final 변수여야 한다.
         * 덕분에 람다는 변수가 아닌 값에 국한되어 어떤 동작을 수행한다는 사실이 명확해진다.
         */
        int portNumber = 1337; // 실질적으로 final처럼 취급
        Runnable r = () -> System.out.println(portNumber);

        /**
         * 메서드 참조는 특정 메서드만을 호출하는 람다의 축약형이라고 생각할 수 있다.
         * 명시적으로 메서드명을 참조함으로써 가독성을 높일 수 있다.
         * 메서드명 앞에 구분자(::)를 붙이는 방식으로 메서드 참조를 활용할 수 있다.
         * 
         * 컴파일러는 람다 표현식의 형식을 검사하던 방식과 비슷한 과정으로
         * 메서드 참조가 주어진 함수형 인터페이스오 호환되는지 확인한다.
         * 즉, 메서드 참조는 콘텍스트의 형식과 일치해야 한다.
         */
        System.out.println("Test method reference");
        List<String> str = Arrays.asList("a", "B", "A", "b");
        // str.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        str.sort(String::compareToIgnoreCase);
        System.out.println(str + "\n");

        /**
         * ClassName::new처럼 클래스명과 new 키워드를 이용해서 기존 생성자의 참조는 만들 수 있다.
         * 
         * Apple(Integer weight)라는 시그니처를 갖는 생성자는 Function 인터페이스와 시그니처와 같다.
         */
        System.out.println("Test constructor reference");
        Supplier<Apple> s1 = Apple::new;
        Apple a1 = s1.get();

        Function<Integer, Apple> s2 = Apple::new;
        Apple a2 = s2.apply(110);
        System.out.println(a2.getWeight() + "\n");

        List<Integer> wieghts = Arrays.asList(7, 4, 4, 10);
        List<Apple> apples = Apple.map(wieghts, Apple::new);
        Apple.prettyPrintApple(apples, new AppleFormatter() {
            public String accept(Apple apple) {
                return "An apple of " + apple.getWeight();
            }
        });
        System.out.println();

        BiFunction<Color, Integer, Apple> s3 = Apple::new;
        Apple a3 = s3.apply(Color.GREEN, 110);
        System.out.printf("%s apple of %d\n\n", a3.getColor(), a3.getWeight());

        // 람다, 메서드 참조 활용하기
        System.out.println("Test lambda and method reference");
        List<Color> colors = Arrays.asList(Color.GREEN, Color.GREEN, Color.RED, Color.RED);
        List<Apple> inventory = Apple.map(colors, wieghts, Apple::new);
        Apple.prettyPrintApple(inventory, new AppleSimpleFormatter());
        System.out.println();

        // {@link AppleComparator}
        // inventory.sort(new AppleComparator());

        // lambda expression
        // inventory.sort((Apple o1, Apple o2) ->
        // o1.getWeight().compareTo(o2.getWeight()));
        // inventory.sort((o1, o2) -> o1.getWeight().compareTo(o2.getWeight()));

        // using comparing method
        // inventory.sort(comparing(apple -> apple.getWeight()));

        // method reference
        inventory.sort(comparing(Apple::getWeight));

        Apple.prettyPrintApple(inventory, new AppleSimpleFormatter());
        System.out.println();

        // Comparator combination
        System.out.println("Test comparator combination");
        inventory.sort(comparing(Apple::getWeight).reversed());
        Apple.prettyPrintApple(inventory, new AppleSimpleFormatter());
        System.out.println();

        inventory = Apple.map(colors, wieghts, Apple::new);
        inventory.sort(comparing(Apple::getWeight)
                .reversed()
                .thenComparing(Apple::getColor));
        Apple.prettyPrintApple(inventory, new AppleSimpleFormatter());
        System.out.println();

        /**
         * Prediacte combination
         * 특정 프리디케이트를 반전시킬 때 negate 메서드를 사용할 수 있다.
         * 또한 and 메서드를 이용해서 빨간색이면서 무거운 사과를 선택하도록 두 람다를 조합할 수 있다.
         * 그뿐만 아니라 or 메서드를 이용해서 다양한 조건을 만들 수 있다.
         */
        Predicate<Apple> redApple = apple -> apple.getColor().equals(Color.RED);
        Predicate<Apple> notRedApple = redApple.negate();

        Predicate<Apple> redAndHeavyApple = redApple.and(apple -> apple.getWeight() > 150);

        Predicate<Apple> redAndHeavyAppleOrGreen = redApple.and(apple -> apple.getWeight() > 150)
                .or(apple -> apple.getColor().equals(Color.GREEN));

        /**
         * Function combination
         * andThen 메서드는 주어진 함수를 먼저 적용한 결과를 다른 함수의 입력으로 전달하는 함수를 반환한다.
         * compose 메서드는 인수로 주어진 함수를 먼저 실행한 다음에 그 결과를 외부 함수의 인수로 제공한다.
         */
        System.out.println("Test function combination");
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;

        System.out.println(f.andThen(g).apply(1)); // g(f(x))

        System.out.println(f.compose(g).apply(1)); // f(g(x))
    }

    public static void process(Runnable r) {
        r.run();
    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("./resources/data.txt"))) {
            return p.process(br);
        }
    }
}

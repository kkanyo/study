package chapter03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

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
 */
public class LambdaTest {
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

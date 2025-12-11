package chapter02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import chapter02.Apple.Color;

public class AppleTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple(Color.GREEN, 80));
        inventory.add(new Apple(Color.RED, 155));
        inventory.add(new Apple(Color.GREEN, 120));

        System.out.println("Test AppleFormatter");
        Apple.prettyPrintApple(inventory, new AppleSimpleFormatter());
        Apple.prettyPrintApple(inventory, new AppleFancyFormatter());
        System.out.println();

        // 로직과 관련 없는 코드가 많이 추가되었다.
        System.out.println("Test ApplePredicate");
        List<Apple> heavyApples = AppleFilter.filterApples(inventory, new AppleHeavyWeightPredicate());
        Apple.prettyPrintApple(heavyApples, new AppleSimpleFormatter());

        List<Apple> greenApples = AppleFilter.filterApples(inventory, new AppleGreenColorPredicate());
        Apple.prettyPrintApple(greenApples, new AppleSimpleFormatter());
        System.out.println();

        /**
         * 클래스의 선언과 인스턴스화를 동시에 수행할 수 있도록 익명 클래스(anonymous class) 기법을 제공한다.
         * 
         * 익명 클래스는 자바의 지역 클래스(local class, 블록 내부에 선언된 클래스)와 비슷한 개념이다.
         * 익명 클래스를 이용하면 클래스 선언과 인스턴스화를 동시에 할 수 있다.
         * 즉, 즉석에서 필요한 구현을 만들어서 사용할 수 있다.
         * 
         * 익명 클래스는 여전히 많은 공간을 차지한다.
         * 코드의 장황함(verbosity)은 구현하고 유지보수하는 데 시간이 오래 걸릴 뿐 아니라
         * 읽는 즐거움을 뺴앗는 요소로, 개발자로부터 외면받는다.
         */
        System.out.println("Test ApplePredicate with anonymous class");
        List<Apple> redApples = AppleFilter.filterApples(inventory, new ApplePredicate() {
            @Override
            public boolean test(Apple apple) {
                return Color.RED.equals(apple.getColor());
            }
        });
        Apple.prettyPrintApple(redApples, new AppleSimpleFormatter());
        System.out.println();

        // 람다 표현식 사용
        System.out.println("Test ApplePredicate with lambda expression");
        redApples = AppleFilter.filterApples(inventory, (Apple apple) -> Color.RED.equals(apple.getColor()));
        Apple.prettyPrintApple(redApples, new AppleSimpleFormatter());
        System.out.println();

        System.out.println("Test generalization Predicate");
        redApples = AppleFilter.filter(inventory, (Apple apple) -> Color.RED.equals(apple.getColor()));
        Apple.prettyPrintApple(redApples, new AppleSimpleFormatter());

        List<Integer> numbers = new ArrayList<>(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        List<Integer> evenNumbers = AppleFilter.filter(numbers, (Integer i) -> i % 2 == 0);
        for (Integer number : evenNumbers) {
            System.out.printf("%d ", number);
        }
        System.out.println("\n");

        // {@link Comparator}
        System.out.println("Test Comparator");
        inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
        Apple.prettyPrintApple(inventory, new AppleSimpleFormatter());
        System.out.println();

        // {@link Runnable}
        System.out.println("Test Runnable");
        Thread t = new Thread(() -> System.out.println("Hello, world!"));
        t.run();
        System.out.println();

        // {@link Callable}
        // {@link ExecutorService} 인터페이스는 태스크 제추과 실행 과정의 연관성을 끊어준다.
        // ExecutorService를 이용하면 태스크를 스레드 풀로 보내고 결과를 {@link Future}로 저장할 수 있다.
        System.out.println("Test Callable");
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> System.out.println("Hello, world!"));
        Future<String> threadName = executorService.submit(() -> Thread.currentThread().getName());
        threadName.get();
    }
}

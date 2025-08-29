package chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;

import chapter02.Apple.Color;

public class AppleTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Apple> inventory = new ArrayList<>();
        inventory.add(new Apple(Color.GREEN, 80));
        inventory.add(new Apple(Color.RED, 155));
        inventory.add(new Apple(Color.GREEN, 75));
    
        prettyPrintApple(inventory, new AppleSimpleFormatter());
        prettyPrintApple(inventory, new AppleFancyFormatter());
        System.out.println();

        // use lambda expression
        List<Apple> redApples = filter(inventory, (Apple apple) -> 
            Color.RED.equals(apple.getColor())
        );

        prettyPrintApple(redApples, new AppleSimpleFormatter());
        System.out.println();

        inventory.sort(
            (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));

        Thread t = new Thread(() -> System.out.println("Hello, world"));
        t.start();

        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> threadName = executorService.submit(
            () -> Thread.currentThread().getName());
        System.out.println(threadName.get());
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> p) {
        List<T> result = new ArrayList<>();

        for (T e: list) {
            if (p.test(e)) {
                result.add(e);
            }
        }

        return result;
    }

    public static void prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
        for (Apple apple: inventory) {
            String output = formatter.accept(apple);
            System.out.println(output);
        }
    }
}

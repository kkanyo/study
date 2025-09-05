package chapter03;

import static java.util.Comparator.comparing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import chapter03.Fruit.Color;

public class LambdaTest {
    public static void main(String[] args) throws IOException {
        String oneLine = processFile((BufferedReader br) -> br.readLine());
        System.out.println(oneLine);
        System.out.println();
        
        String twoLine = processFile((BufferedReader br) -> br.readLine() + "\n" + br.readLine());
        System.out.println(twoLine);
        System.out.println();

        List<Integer> l = map(Arrays.asList("lambdas", "in", "action"),
            (String s) -> s.length());
        System.out.println(l);
        System.out.println();

        // method reference
        System.out.println("메소드 참조");
        List<String> str1 = Arrays.asList("a", "b", "A", "B");
        List<String> str2 = Arrays.asList("a", "b", "A", "B");
        str1.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
        str2.sort(String::compareToIgnoreCase);
        System.out.println(str1);
        System.out.println(str2);
        System.out.println();

        // constructor reference
        System.out.println("생성자 참조");
        TriFunction<Color, Integer, Integer, Fruit> fruitFactory = Fruit::new;
        Fruit apple = fruitFactory.apply(Color.GREEN, 155, 3);
        apple.printFruit();
        System.out.println();

        List<Fruit> inventory = new ArrayList<>();
        inventory.add(new Fruit(Color.RED, 80, 1));
        inventory.add(new Fruit(Color.RED, 155, 1));
        inventory.add(new Fruit(Color.GREEN, 75, 1));
        inventory.add(new Fruit(Color.GREEN, 80, 1));

        System.out.println("람다를 이용한 정렬");
        // inventory.sort(new FruitComparator());

        // inventory.sort(new Comparator<Fruit>() {
        //     public int compare(Fruit o1, Fruit o2) {
        //         return o1.getWeight().compareTo(o2.getWeight());
        //     }
        // });

        // inventory.sort((o1, o2) -> o1.getWeight().compareTo(o2.getWeight()));

        // inventory.sort(comparing(fruit -> fruit.getWeight()));

        inventory.sort(comparing(Fruit::getWeight));

        printFruits(inventory);

        System.out.println("역정렬");
        inventory.sort(comparing(Fruit::getWeight).reversed());

        printFruits(inventory);


        // connect Comperator
        inventory.sort(comparing(Fruit::getWeight)
            .thenComparing(Fruit::getColor));
        
        printFruits(inventory);

        // combine Predicate
        Predicate<Fruit> redFruit = (fruit) -> fruit.getColor() == Color.RED;
        Predicate<Fruit> notRedFruit = redFruit.negate();

        List<Fruit> notRedFruits = filter(inventory, notRedFruit);
        printFruits(notRedFruits);

        Predicate<Fruit> redAndHeavyFruit = redFruit.and(fruit -> fruit.getWeight() > 150);
        List<Fruit> redAndHeavyFruits = filter(inventory, redAndHeavyFruit);
        printFruits(redAndHeavyFruits);

        Predicate<Fruit> redAndHeavyFruitOrGreen = redAndHeavyFruit
            .or(fruit -> Color.GREEN.equals(fruit.getColor()));
        List<Fruit> redAndHeavyFruitsOrGreen = filter(inventory, redAndHeavyFruitOrGreen);
        printFruits(redAndHeavyFruitsOrGreen);

        // combine Function
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = x -> x * 2;
        Function<Integer, Integer> h = f.andThen(g);    // g(f(x))
        int result = h.apply(1);
        System.out.println(result);
        System.out.println();

        h = f.compose(g);   // f(g(x))
        result = h.apply(1);
        System.out.println(result);
        System.out.println();

    }

    public static String processFile(BufferedReaderProcessor p) throws IOException {
        try (BufferedReader br = 
                new BufferedReader(new FileReader("./resources/data.txt"))) {
            return p.process(br);
        }
    }

    public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();

        for (T t: list) {
            result.add(f.apply(t));
        }

        return result;
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

    private static void printFruits(List<Fruit> list) {
        list.forEach(e -> e.printFruit());
        System.out.println();
    }
}

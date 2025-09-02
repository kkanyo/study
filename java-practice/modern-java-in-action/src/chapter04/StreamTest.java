package chapter04;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.IntSupplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import chapter04.Dish.Type;

public class StreamTest {
    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
            new Dish("prok", false, 800, Dish.Type.MEAT),
            new Dish("beef", false, 700, Dish.Type.MEAT),
            new Dish("chicken", false, 400, Dish.Type.MEAT),
            new Dish("french fries", true, 530, Dish.Type.OTHER),
            new Dish("rice", true, 350, Dish.Type.OTHER),
            new Dish("season fruit", false, 120, Dish.Type.OTHER),
            new Dish("pizza", false, 550, Dish.Type.OTHER),
            new Dish("prawns", false, 300, Dish.Type.FISH),
            new Dish("salmon", false, 450, Dish.Type.FISH)
        );

        List<String> names = menu.stream()
            .filter(dish -> {
                System.out.println("filtering: " + dish.getName());
                return dish.getCalories() > 300;
            })
            .map(dish -> {
                System.out.println("mapping: " + dish.getName());
                return dish.getName();
            })
            .limit(3)
            .toList();
        System.out.println(names);
        System.out.println();

        List<String> highCaloricDish = menu.stream()
            .filter(dish -> dish.getCalories() > 300)
            .map(Dish::getName)
            .toList();
        System.out.println(highCaloricDish);
        System.out.println();

        // filtering
        System.out.println("[stream filtering]");
        List<Integer> numbers = Arrays.asList(1, 2, 1, 3 ,3, 2, 4);
        numbers.stream()
            .filter(i -> i % 2 == 0)
            .distinct()
            .forEach(System.out::println);
        System.out.println();

        // slicing
        System.out.println("[stream slicing]");
        menu.stream()
            .takeWhile(dish -> dish.getCalories() > 500)
            .forEach(Dish::printDish);
        System.out.println();
            
        menu.stream()
            .dropWhile(dish -> dish.getCalories() > 500)
            .forEach(Dish::printDish);
        System.out.println();

        // skip
        System.out.println("[stream skip]");
        menu.stream()
            .filter(d -> d.getCalories() > 300)
            .skip(2)
            .forEach(Dish::printDish);
        System.out.println();

        menu.stream()
            .filter(d -> d.getType() == Type.MEAT)
            .limit(2)
            .forEach(Dish::printDish);
        System.out.println();

        // mapping
        System.out.println("[stream mapping]");
        List<Integer> dishNameLengths = menu.stream()
            .map(Dish::getName)
            .map(String::length)
            .toList();
        System.out.println(dishNameLengths);
        System.out.println();

        // flattening
        List<String> words = Arrays.asList("Goodbye", "World");
        words.stream()
            .map(word -> word.split(""))
            .flatMap(Arrays::stream)
            .distinct()
            .forEach(System.out::print);
        System.out.println("\n");

        numbers = Arrays.asList(1, 2, 3, 4, 5);
        numbers.stream()
            .map(num -> num * num)
            .forEach(num -> System.out.printf("%d ", num));
        System.out.println("\n");

        // searching
        System.out.println("[stream searching]");
        if (menu.stream().anyMatch(Dish::isVegetarian)) {
            System.out.println("The menu is (somewhat) vegetarian friendly!!");
        }
        System.out.println();

        System.out.println(menu.stream()
            .allMatch(dish -> dish.getCalories() < 1000));
        System.out.println();

        System.out.println(menu.stream()
            .noneMatch(dish -> dish.getCalories() >= 1000));
        System.out.println();

        Optional<Dish> dish = menu.stream()
            .filter(Dish::isVegetarian)
            .findAny();
        System.out.println(dish.get().getName());
        System.out.println();

        numbers.stream()
            .map(n -> n * n)
            .filter(n -> n % 3 == 0)
            .findFirst()
            .ifPresent(n -> System.out.println(n));
        System.out.println();

        // Reducing
        System.out.println("[stream reducing]");
        numbers = Arrays.asList(4, 5, 3, 9);
        int sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println(sum + "\n");

        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        System.out.println(product + "\n");

        Optional<Integer> sumOptional = numbers.stream().reduce(Integer::sum);  // no init value
        System.out.println(sumOptional.get() + "\n");

        Optional<Integer> max = numbers.stream().reduce(Integer::max);
        System.out.println(max.get() + "\n");

        int menuCount = menu.stream()
            .map(d -> 1)
            .reduce(0, (a, b) -> a + b);
        System.out.println(menuCount + "\n");

        // Primitive stream
        System.out.println("[primitive stream]");
        int calories = menu.stream()
            .mapToInt(Dish::getCalories)    // return InStream (not Stream<Integer>)
            .sum();
        System.out.printf("total calories: %d\n\n", calories);

        IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
        Stream<Integer> stream = intStream.boxed();

        OptionalInt maxCalories = menu.stream()
            .mapToInt(Dish::getCalories)
            .max();
        System.out.printf("max calorie: %d\n\n", maxCalories.orElse(1));

        IntStream evenNumbers = IntStream.range(1, 100)
            .filter(n -> n % 2 == 0);
        System.out.printf("count even numbers(range): %d\n", evenNumbers.count());
        
        evenNumbers = IntStream.rangeClosed(1, 100)
            .filter(n -> n % 2 == 0);
        System.out.printf("count even numbers(rangeClosed): %d\n\n", evenNumbers.count());
        
        Stream<double[]> pythahoreanTriples = 
            IntStream.rangeClosed(1, 100)
                .boxed()
                .flatMap(a -> IntStream.rangeClosed(a, 100)
                                    .mapToObj(b -> new double[]{a, b, Math.sqrt(a * a + b * b)})
                                    .filter(t -> t[2] % 1 == 0));
        pythahoreanTriples.limit(5)
            .forEach(t -> System.out.println(t[0] + ", " + t[1] + ", " + t[2]));
        System.out.println();

        // create stream
        System.out.println("[create stream]");
        
        Stream<String> streamString = Stream.of("Modern ", "Java ", "In ", "Action");
        streamString.map(String::toUpperCase)
            .forEach(System.out::println);
        System.out.println();

        streamString = Stream.empty();
        streamString.forEach(System.out::println);
        System.out.println();

        Stream.of("config", "home", "user")
            .flatMap(key -> Stream.ofNullable(System.getProperty(key)))
            .forEach(System.out::println);

        int[] arrNumbers = {2, 3, 5, 7, 11, 13};
        System.out.printf("arrNumbers sum: %d\n\n", Arrays.stream(arrNumbers).sum());

        long uniqueWords = 0;
        // Stream interface extends AutoCloseable interface, so it can manage resource automatically
        try (Stream<String> lines = Files.lines(Paths.get("./resources/data.txt"), Charset.defaultCharset())) {
            uniqueWords = lines.flatMap(line -> Arrays.stream(line.split("")))
                .distinct()
                .count();
        }
        catch (IOException e) {
            System.out.println(e);
        }
        System.out.printf("count of unique words in 'data.txt': %d\n\n", uniqueWords);

        Stream.iterate(0, n -> n + 2)
            .limit(10)
            .forEach(n -> System.out.printf("%d ", n));
        System.out.println("\n");

        Stream.iterate(new int[]{0, 1}, arr -> new int[]{arr[1], arr[0] + arr[1]})
            .limit(20)
            .map(t -> t[0])
            .forEach(n -> System.out.printf("%d ", n));
        System.out.println("\n");

        IntStream.iterate(0, n -> n < 100, n -> n + 4)
            .forEach(n -> System.out.printf("%d ", n));
        System.out.println("\n");

        IntStream.iterate(0, n -> n + 4)
            // .filter(n -> n < 100)    // infinite loop
            .takeWhile(n -> n < 100)
            .forEach(n -> System.out.printf("%d ", n));
        System.out.println("\n");

        Stream.generate(Math::random)
            .limit(5)
            .forEach(n -> System.out.printf("%.2f ", n));
        System.out.println("\n");

        // not thread-safety
        IntStream.generate(new IntSupplier() {
            private int prev = 0;
            private int cur = 1;
            
            public int getAsInt() {
                int oldPrev = this.prev;
                int next = this.prev + this.cur;
                
                this.prev = this.cur;
                this.cur = next;

                return oldPrev;
            }
        }).limit(10)
            .forEach(n -> System.out.printf("%d ", n));
        System.out.println("\n");
    }
}

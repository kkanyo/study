package chapter02;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Apple {
    public enum Color {
        RED, GREEN
    }

    Color color;
    Integer weight;

    public Apple(Color color, Integer weight) {
        this.color = color;
        this.weight = weight;
    }

    public Apple(Integer wieght) {
        this.weight = wieght;
    }

    public Apple() {

    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Color getColor() {
        return color;
    }

    public Integer getWeight() {
        return weight;
    }

    public static void prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
        for (Apple apple : inventory) {
            String output = formatter.accept(apple);

            System.out.println(output);
        }
    }

    public static List<Apple> map(List<Integer> list, Function<Integer, Apple> f) {
        List<Apple> result = new ArrayList<>();
        for (Integer i : list) {
            result.add(f.apply(i));
        }
        return result;
    }

    public static List<Apple> map(List<Color> colors, List<Integer> weights, BiFunction<Color, Integer, Apple> f) {
        List<Apple> result = new ArrayList<>();
        for (int i = 0; i < colors.size(); i++) {
            result.add(f.apply(colors.get(i), weights.get(i)));
        }
        return result;
    }
}

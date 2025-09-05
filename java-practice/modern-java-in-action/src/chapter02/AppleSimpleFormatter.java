package chapter02;

public class AppleSimpleFormatter implements AppleFormatter {
    @Override
    public String accept(Apple apple) {
        return "An " + apple.getColor().name() + " apple of " + apple.getWeight() + "g";
    }
}

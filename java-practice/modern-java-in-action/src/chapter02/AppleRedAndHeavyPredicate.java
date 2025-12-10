package chapter02;

import chapter02.Apple.Color;

public class AppleRedAndHeavyPredicate implements ApplePredicate {

    @Override
    public boolean test(Apple apple) {
        return Color.RED.equals(apple.getColor())
                && apple.getWeight() > 150;
    }

}

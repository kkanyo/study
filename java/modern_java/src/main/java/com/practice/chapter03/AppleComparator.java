package com.practice.chapter03;

import java.util.Comparator;

import com.practice.chapter02.Apple;

public class AppleComparator implements Comparator<Apple> {

    @Override
    public int compare(Apple a1, Apple a2) {
        return a1.getWeight().compareTo(a2.getWeight());
    }
}

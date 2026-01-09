package com.practice.chapter07;

import java.util.Spliterator;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ParallelStreamTest {

    public static void main(String[] args) {
        ParallelStream.test();

        System.out.println(
                "ForkJoin sum done in" + measurePerf(
                        ForkJoinSumCalculator::forkJoinSum, 10_000_000L) + " msecs\n");

        final String SENTENCE = "Nel       mezzo del cammin di nostra vita " +
                "mi ritrovai in una    selva oscura " +
                "ch  la dritta via era   smarrita ";

        System.out.printf("[Iteratively] Found %d words...\n", countWrodIteratrively(SENTENCE));

        Stream<Character> stream = IntStream.range(0, SENTENCE.length())
                .mapToObj(SENTENCE::charAt);
        System.out.printf("[Stream] Found %d words...\n", countWords(stream));

        // 스트림 분할 위치에 따라 잘못된 결과가 나올 수 있다.
        Stream<Character> parallelStream = IntStream.range(0, SENTENCE.length())
                .mapToObj(SENTENCE::charAt)
                .parallel();
        System.out.printf("[Parallel stream] Found %d words...\n", countWords(parallelStream));

        Spliterator<Character> spliterator = new WordCounterSpliterator(SENTENCE);
        Stream<Character> spliteratorStream = StreamSupport.stream(spliterator, true);

        System.out.printf("[Spliterator parallel stream] Found %d words...\n", countWords(spliteratorStream));

    }

    public static <T, R> long measurePerf(Function<T, R> f, T input) {
        long fastest = Long.MAX_VALUE;

        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();
            R result = f.apply(input);
            long duration = (System.nanoTime() - start) / 1_000_000;

            System.out.println("Result: " + result);

            if (duration < fastest) {
                fastest = duration;
            }
        }

        return fastest;
    }

    public static int countWrodIteratrively(String s) {
        int counter = 0;
        boolean lastSpace = true;

        for (char c : s.toCharArray()) {
            if (Character.isWhitespace(c)) {
                lastSpace = true;
            } else {
                if (lastSpace) {
                    counter++;
                    lastSpace = false;
                }
            }
        }

        return counter;
    }

    public static int countWords(Stream<Character> stream) {
        WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
                WordCounter::accumulate,
                WordCounter::combine);
        return wordCounter.getCounter();
    }
}
package chapter05;

import static java.util.Comparator.comparing;

import java.util.Arrays;
import java.util.List;

public class PracticeStreamTest {
    public static void main(String[] args) {
        Filtering.test();

        Mapping.test();

        SearchingMatching.test();

        Reducing.test();

        // Practice
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transaction = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 300),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950));

        // 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
        transaction.stream()
                .filter(tr -> tr.getYear() == 2011)
                .sorted(comparing(Transaction::getValue))
                .forEach(tr -> System.out.println(tr.toString()));
        System.out.println();

        // 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
        transaction.stream()
                .map(tr -> tr.getTrader().getCity())
                .distinct()
                .forEach(System.out::println);
        System.out.println();

        // 3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
        transaction.stream()
                .map(tr -> tr.getTrader())
                .filter(tr -> tr.getCity().equals("Cambridge"))
                .sorted(comparing(Trader::getName))
                .forEach(tr -> System.out.println(tr.toString()));
        System.out.println();

        // 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
        transaction.stream()
                .map(tr -> tr.getTrader().getName())
                .distinct()
                .sorted()
                .forEach(System.out::println);
        System.out.println();

        // 5. 밀라노에 거래자가 있는가?
        if (transaction.stream().anyMatch(tr -> tr.getTrader().getCity().equals("Milano"))) {
            System.out.println("True");
        } else {
            System.out.println("False");
        }
        System.out.println();

        // 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
        transaction.stream()
                .filter(tr -> tr.getTrader().getCity().equals("Cambridge"))
                .forEach(tr -> System.out.println(tr.toString()));
        System.out.println();

        // 7. 전체 트랜잭션 중 최댓값은 얼마인가?
        transaction.stream()
                .max(comparing(Transaction::getValue))
                .ifPresent(System.out::println);
        System.out.println();

        // 8. 전체 트랜잭션 중 최솟값은 얼마인가?
        transaction.stream()
                .map(tr -> tr.getValue())
                .reduce(Integer::min)
                .ifPresent(System.out::println);
        System.out.println();
    }
}

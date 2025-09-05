package chapter05;

import static java.util.Comparator.comparing;

import java.util.List;

public class PracticeStream {
    public void sortTransactionValueInYear(List<Transaction> list, int year) {
        list.stream()
            .filter(tr -> tr.getYear() == year)
            .sorted(comparing(Transaction::getValue))
            .forEach(tr -> System.out.println(tr.toString())); 
        System.out.println();
    }

    public void distinctCity(List<Transaction> list) {
        list.stream()
            .map(tr -> tr.getTrader().getCity())
            .distinct()
            .forEach(city -> System.out.printf("%s ", city));
        System.out.println("\n");
    }

    public void sortTraderNameAtCity(List<Transaction> list, String city) {
        list.stream()
            .map(Transaction::getTrader)
            .filter(tr -> tr.getCity().equals(city))
            .distinct()
            .sorted(comparing(Trader::getName))
            .forEach(tr -> System.out.println(tr.toString()));
        System.out.println();
    }

    public void sortTraderName(List<Transaction> list) {
        list.stream()
            .map(tr -> tr.getTrader().getName())
            .distinct()
            .sorted(String::compareTo)
            // .toList()
            .forEach(name -> System.out.printf("%s ", name));
        System.out.println("\n");
    }

    public void hasCityTrader(List<Transaction> list, String city) {
        if (list.stream().anyMatch(tr -> tr.getTrader().getCity().equals(city))) {
            System.out.printf("has trader in %s\n\n", city);
        } else {
            System.out.println("none");
        }
    }

    public void printTransactionValueInCity(List<Transaction> list, String city) {
        list.stream()
            .filter(tr -> tr.getTrader().getCity().equals(city))
            .forEach(tr -> System.out.println(tr.getValue()));
        System.out.println();
    }

    public void maxTransactionValue(List<Transaction> list) {
        list.stream()
            .max(comparing(Transaction::getValue))
            .ifPresent(tr -> System.out.println(tr.getValue()));
        System.out.println();
    }

    public void minTransactionValue(List<Transaction> list) {
        list.stream()
            .min(comparing(Transaction::getValue))
            .ifPresent(tr -> System.out.println(tr.getValue()));
        System.out.println();
    }
}

package chapter05;

import java.util.Arrays;
import java.util.List;

public class PracticeStreamTest {
    public static void main(String[] args) {
        PracticeStream practiceStream = new PracticeStream();

        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
            new Transaction(brian, 2011, 300),
            new Transaction(raoul, 2012, 1000),
            new Transaction(raoul, 2011, 400),
            new Transaction(mario, 2012, 710),
            new Transaction(mario, 2012, 700),
            new Transaction(alan, 2012, 950)
        );

        practiceStream.sortTransactionValueInYear(transactions, 2011);

        practiceStream.distinctCity(transactions);

        practiceStream.sortTraderNameAtCity(transactions, "Cambridge");

        practiceStream.sortTraderName(transactions);

        practiceStream.hasCityTrader(transactions, "Milan");

        practiceStream.printTransactionValueInCity(transactions, "Cambridge");

        practiceStream.maxTransactionValue(transactions);

        practiceStream.minTransactionValue(transactions);
    }
}

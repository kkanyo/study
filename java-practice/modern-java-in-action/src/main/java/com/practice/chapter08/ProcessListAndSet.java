package com.practice.chapter08;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * 리스트와 집합 처리
 * 
 * - removeIf: 프레디케이트를 만족하는 요소를 제거한다.
 * List나 Set을 구현하거나 그 구현을 상속받은 모든 클래스에서 이용할 수 있다.
 * - replaceAll: 리스트에서 이용할 수 있는 기능으로 UnaryOperator 함수를 이용해 요소를 바꾼다.
 * - sort: List 인터페이스에서 지공하는 기능으로 리스트를 정렬한다.
 * 이들 메서드는 호출한 컬렉션 자체를 바꾼다.
 * 새로운 결과를 만드는 스트림 동작과 달리 이들 메서드는 기존 컬렉션을 바꾼다.
 * 
 * 켈력선을 바꾸는 동작은 에러를 유발하며 복잡함을 더하기 때문에 추가되었다. ???
 */
public class ProcessListAndSet {
    public static void main(String[] args) {
        // removeIf 메서드
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("01Raphael"));
        transactions.add(new Transaction("Olivia"));
        transactions.add(new Transaction("01Raphael"));
        transactions.add(new Transaction("Virten"));
        System.out.println(transactions);

        try {
            for (Transaction transaction : transactions) {
                if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
                    transactions.remove(transaction);
                }
            }
        } catch (Exception e) {
            System.out.println(transactions);
            e.printStackTrace();
        }

        transactions.add(new Transaction("01Raphael"));

        /**
         * 내부적으로 for-each 루프는 Iterator 객체를 사용하므로 위 코드는 다음과 같이 해석된다.
         * 
         * 두 개의 개별 객체가 컬렉션을 관리한다는 사실을 주목하자.
         * - Iterator 객체, next(), hasNext()를 이용해 소스를 질의한다.
         * - COllection 객체 자체, remove()를 호출해 요소를 삭제한다.
         */
        try {
            for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
                Transaction transaction = iterator.next();

                if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
                    transactions.remove(transaction);
                }
            }
        } catch (Exception e) {
            System.out.println(transactions);
            e.printStackTrace();
        }

        transactions.add(new Transaction("01Raphael"));

        /**
         * 결과적으로 반복자의 상태는 컬렉션의 상태와 서로 동기화되지 않는다.
         * Iterator 객체를 명시적으로 사용하고 그 객체의 remove() 메서드를 호출함으로 이 문제를 해결할 수 있다.
         * 이 코드 패턴은 Java 8의 removeIf 메서드로 바꿀 수 있다.
         */
        for (Iterator<Transaction> iterator = transactions.iterator(); iterator.hasNext();) {
            Transaction transaction = iterator.next();

            if (Character.isDigit(transaction.getReferenceCode().charAt(0))) {
                iterator.remove();
            }
        }
        System.out.println(transactions);

        transactions.add(new Transaction("01Raphael"));
        transactions.removeIf(transaction -> Character.isDigit(transaction.getReferenceCode().charAt(0)));
        System.out.println(transactions + "\n");

        // replaceAll 메서드
        // List 인터페이스의 replaceAll 메서드를 이용해 리스트의 각 요소를 새로운 요소로 바꿀 수 있다.
        List<String> referenceCodes = new ArrayList<>();
        referenceCodes.add("a12");
        referenceCodes.add("C14");
        referenceCodes.add("b13");

        // 아래 코드는 새 문자열 컬렉션을 만든다.
        List<String> newReferenceCodes = referenceCodes.stream()
                .map(code -> Character.toUpperCase(code.charAt(0)) +
                        code.substring(1))
                .toList();
        System.out.println(newReferenceCodes);
        System.out.println(referenceCodes);

        // 원하는 것은 기존 컬렉션을 바꾸는 것이다.
        // 다음처럼 ListIterator 객체(요소를 바꾸는 set() 메서드를 지원)를 이용할 수 있다.
        // 컬렉션 객체를 Iterator 객체와 혼용하면 반복과 컬렉션 변경이 동시에 이루어지면서 쉽게 문제를 일으킨다.
        for (ListIterator<String> iterator = referenceCodes.listIterator(); iterator.hasNext();) {
            String code = iterator.next();
            iterator.set(Character.toUpperCase(code.charAt(0)) + code.substring(1));
        }
        System.out.println(referenceCodes);

        referenceCodes.clear();
        referenceCodes.add("a12");
        referenceCodes.add("C14");
        referenceCodes.add("b13");

        referenceCodes.replaceAll(code -> Character.toUpperCase(code.charAt(0)) + code.substring(1));
        System.out.println(referenceCodes);
    }

    private static class Transaction {
        private String referenceCode;

        public Transaction(String referenceCode) {
            this.referenceCode = referenceCode;
        }

        public String getReferenceCode() {
            return referenceCode;
        }

        @Override
        public String toString() {
            return getReferenceCode();
        }
    }
}

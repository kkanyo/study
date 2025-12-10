package chapter02;

/**
 * 참 또는 거짓을 반환하는 함수를 `프리디케이트(Predicate)`라고 한다.
 * 
 * 선택 조건을 결정하는 인터페이스를 정의하자.
 * 
 * 다양한 선택 조건을 대표하는 여러 버전의 ApplePredicate를 정의할 수 있다.
 * {@link AppleHeavyWeightPredicate}
 * {@link AppleGreenColorPredicate}
 * 
 * ApplePredicate는 사과 선택 전략을 캡슐화한다.
 * 이를 `전략 디자인 패턴(strategy design pattern)`이라고 부른다.
 * 각 알고리즘(전략이라 불리는)을 캡슐화하는 알고리즘 패밀리를 정의해둔 다음에 런타임에 알고리즘을 선택하는 기법이다.
 * 여기서 ApplePrediacte가 알고리즘 패밀리고, AppleHeavyWeightPredicate와
 * AppleGreenColorPredicate가 전략이다.
 * 
 * 다양한 동작을 수행하기 위해서는
 * {@link AppleFilter#filterApples(java.util.List, ApplePredicate)}
 * 에서 ApplePredicate 객체를 받아 애플의 조건을 검사하도록 한다.
 * 이렇게 `동작 파라미터화`, 즉 메서드가 다양한 동작(또는 전략)을 받아서 내부적으로 다양한 동작을 수행할 수 있다.
 * 컬렉션 탐색 로직과 각 항목에 적용할 동작을 분리할 수 있다는 것이 동작 파라미터화의 강점이다.
 */
public interface ApplePredicate {
    boolean test(Apple apple);
}
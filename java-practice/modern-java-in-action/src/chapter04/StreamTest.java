package chapter04;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@link java.util.stream.Stream}
 * 스트림의 장점
 * - 제어 블록을 사용해서 어떻게 동작을 구현할지 지정할 필요 없이 동작의 수행을 지정할 수 있다.
 * - 여러 빌딩 블록 연산을 연결해서 복잡한 데이터 처리 파이프라인을 만들 수 있다.
 * 
 * filter(또는 sorted, map, collect) 같은 연산은 고수준 빌딩 블록(high-level building block,
 * 복잡한 내부 동작을 신경쓰지 않고 이미 만들어진 기능을 가져다 쓸 수 있도록 추상화된 SW 구성 요소)으로
 * 이루어져 있으므로 특정 스레딩 모델에 제한되지 않고 자유롭게 어떤 상황에서든 사용할 수 있다.
 * 
 * 또한 이들은 내부적으로 단일 스레드 모델에 사용할 수 있지만 멀티코어 아키텍쳐를 최대한 투명하게
 * 활용할 수 있게 구현되어 있다.
 * 
 * 결과적으로 데이터 처리 과정을 병렬화하면서 스레드와 락을 걱정할 필요가 없다.
 * 
 * 스트림 API의 특징 요약
 * - 선언형: 더 간결하고 가독성이 좋아진다.
 * - 조립할 수 있음: 유연성이 좋아진다.
 * - 병렬화: 성능이 좋아진다.
 * 
 * 스트림(Stream)이란 데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소(Sequence of elements)로
 * 정의할 수 있다.
 * - 연속된 요소: 컬렉션과 마찬가지로 스트림은 특정 요소 형식으로 이루어진 연속된 값 집합의 인터페이스를 제공한다.
 * 컬렉션은 자료구조이므로 시간과 공간의 복잡성과 관련된 요소 저장 및 접근 연산이 주를 이룬다.
 * 반면 스트림은 filter, sorted, map처럼 표현 계산식이 주를 이룬다.
 * 즉, 컬렉션의 주제는 데이터고 스트림의 주제는 계산이다.
 * - 소스: 스트림은 컬렉션, 배열, I/O 자원 등의 데이터 제공 소스로부터 데이터를 소비한다.
 * 정렬된 컬렉션으로 스트림을 생성하면 정렬이 그대로 유지된다.
 * - 데이터 처리 연산: 스트림은 함수형 프로그래밍 언어에서 일반적으로 지원하는 연산과 데이터베이스와 비슷한 연산을
 * 지원한다.
 * 스트림 연산은 순차적으로 또는 병렬로 실행할 수 있다.
 * 
 * 스트림의 중요 특성
 * - 파이프라이닝(Pipelining): 대부분의 스트림 ㅇ녀산은 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수
 * 있도록 스트림 자신을 반한다.
 * 그 덕분에 게으름(laizness), 쇼트서킷(short circuiting)같은 최적화도 얻을 수 있다. (5장 참고)
 * - 내부 반복: 반복자를 이용해서 명시적으로 반복하는 컬렉션과 달리 스트림은 내부 반복자를 지원한다.
 * 
 * 자바의 기존 컬렉션와 새로운 스트림 모두 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스를 제공한다.
 * 여기서 연속된(sequenced)이라는 표현은 순서와 상관없이 아무 값에나 접속하는 것이 아니라 순차적으로
 * 값에 접근한다는 것을 의미한다.
 * 
 * 
 * ** 스트림과 컬렉션 **
 * 데이터를 언제 계산하느냐가 컬렉션과 스트림의 가장 큰 차이다.
 * 
 * 컬렉션은 현재 자료구조가 포함하는 '모든' 값을 메모리에 저장하는 자료구조이다.
 * 즉, 컬렉션의 모든 요소는 컬렉션에 추가하기 전에 계산되어야 한다.
 * 
 * 반면 스트림은 이론적으로 요청할 때만 요소를 계산하는 고정된 자료구조이다. (스트림에 요소를 추가하거나 스트림에서
 * 요소를 제거할 수 없다.)
 * 사용자가 요청하는 값만 스트림에서 추출한다는 것이 핵심이다.
 * 결과적으로 스틀밍느 생산자(producer)와 소비자(consumer) 관계를 형성한다.
 * 
 * 또한 스트림은 게으르게 만들어지는 컬렉션과 같다.
 * 즉, 사용자가 데이터를 요청할 때만 값을 계산한다. (경영학에서는 요청 중심 제조, demand-driven manufactoring
 * 또는 즉석 제조(just-in-time manufacturing)라고 부른다.
 * 
 * 반면 컬렉션은 적극적으로 생성된다.(생산자 중심, supplier-driven: 팔기도 전에 창고를 가득 채운다.)
 * 
 * 
 * 반복자와 마찬가지로 스트림도 '한 번만' 탐색할 수 있다. 즉, 탐색된 스트림의 요소는 소비된다.
 * 
 * 
 * 컬렉션 인터페이스를 사용하려면 사용자가 직접 명시적으로 요소를 반복해야 한다. (예를 들면 for-each 등을 사용해서)
 * 이를 외부 반복(external iteration)이라고 한다.
 * 
 * 반면 스트림 라이브러리는 내부 반복(internal iteration: 반복을 알아서 처리하고 결과 스트림값을 어딘가에
 * 저장해주는)을 사용한다.
 * 
 * 내부 반복을 이용하면 작업을 투명하게 병렬로 처리하거나 더 최적화된 다양한 순서로 처리할 수 있다.
 * 스트림 라이브러리의 내부 반복은 데이터 표현과 하드웨어를 활용한 병렬성 구현을 자동으로 선택한다.
 * 반면 for-each를 이용하는 외부 반복에서는 병렬성을 스스로 관리해야 한다.
 * 
 * 
 * ** 스트림 연산 **
 * 연결할 수 있는 스트림 연산을 중간 연산(itermediate operation)이라고 하며, 스트림을 닫는 연산을 최종 연산(
 * terminal operation)이라고 한다.
 * 
 * filter나 sorted 같은 중간 연산은 다른 스트림을 반환한다.
 * 중간 연산의 중요한 특징은 단말 연산을 스트림 파이프라인에 실행하기 전까지는 아무 연산도 수행하지 않는다는 것, 즉
 * 게으르다(lazy)는 것이다.
 * 중간 연산을 합친 다음에 합쳐진 중간 연산을 최종 연산으로 한 번에 처리하기 때문이다.
 * 
 * 
 * 최종 연산은 스트림 파이프라인에서 결과를 도출한다.
 * 보통 최정 연산에 의해 스트림 의외의 결과가 반환된다.
 * 
 * 
 * 스트림 이용 과정은 다음고 같이 세 가지로 요약할 수 있다.
 * - 질의를 수행할 (컬렉션 같은) 데이터 소스
 * - 스트림 파이프라인을 구성할 중간 연산 연결
 * - 스트림 파이프라인을 실행하고 결과를 만들 최종 연산
 * 
 * 스트림 파이프라인의 개념은 빌더 패턴(builder pattern)과 비슷하다.
 */
public class StreamTest {
    @SuppressWarnings("unused")
    public static void main(String[] args) {
        List<Dish> menu = Arrays.asList(
                new Dish("prok", false, 800, Dish.Type.MEAT),
                new Dish("beef", false, 700, Dish.Type.MEAT),
                new Dish("chicken", false, 400, Dish.Type.MEAT),
                new Dish("french fries", true, 530, Dish.Type.OTHER),
                new Dish("rice", true, 350, Dish.Type.OTHER),
                new Dish("season fruit", false, 120, Dish.Type.OTHER),
                new Dish("pizza", false, 550, Dish.Type.OTHER),
                new Dish("prawns", false, 300, Dish.Type.FISH),
                new Dish("salmon", false, 450, Dish.Type.FISH));

        /**
         * (여러 데모, 디버깅 기법과 마찬가지로 제품 코드에는 출력 코드를 추가하지 않는 것이 좋다.)
         * 
         * 스트림의 게으른 특성 덕분에 몇 가지 최적화 효과를 얻을 수 있다.
         * - 300 칼로리가 넘는 요리는 여러 개지만 오직 처음 3개만 선택되었다.
         * 이는 limit 연산 그리고 쇼트서킷이라 불리는 기법 덕분이다. (5장 참고)
         * - filter와 map은 서로 다른 연산이지만 한 과정으로 병합되었다.
         * 이 기법을 루프 퓨전(loop fusion)이라고 한다.
         */
        List<String> names = menu.stream()
                .filter(dish -> {
                    System.out.println("filtering:" + dish.getName());
                    return dish.getCalories() > 300;
                })
                .map(dish -> {
                    System.out.println("map:" + dish.getName());
                    return dish.getName();
                })
                .limit(3)
                .collect(Collectors.toList());
        System.out.println();

        menu.stream().forEach(System.out::println);
    }

}

package chapter03;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 함수형 인터페이스는 확인된 예외를 던지는 동작을 허용하지 않는다.
 * 즉, 예외를 던지는 람다 표현식을 만들려면 확인된 예외를 선언하는 함수형 인터페이스를 직접 정의하거나
 * 람다를 try/catch 블록으로 감싸야 한다.
 */
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}

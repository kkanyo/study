package learningtest.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Proxy;

import org.junit.jupiter.api.Test;

import tobyspring.learningtest.proxy.Hello;
import tobyspring.learningtest.proxy.HelloTarget;
import tobyspring.learningtest.proxy.HelloUppercase;
import tobyspring.learningtest.proxy.UppercaseHandler;

public class ProxyTest {
    @Test
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("Toby")).isEqualTo("Hello, Toby");
        assertThat(hello.sayHi("Toby")).isEqualTo("Hi, Toby");
        assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank you, Toby");

        Hello proxiedHello = new HelloUppercase(new HelloTarget());
        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO, TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI, TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU, TOBY");        
    }
}

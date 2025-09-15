package learningtest.jdk.proxy;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Proxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import tobyspring.learningtest.proxy.Hello;
import tobyspring.learningtest.proxy.HelloTarget;
import tobyspring.learningtest.proxy.UppercaseHandler;

public class DynamicProxyTest {
    @Test
    public void simpleProxy() {
        Hello proxiedHello = (Hello)Proxy.newProxyInstance(
            getClass().getClassLoader(),                // 동적으로 생성되는 다이나믹 프록시 클래스의 로딩에 사용할 클래스 로더
            new Class[] { Hello.class },                // 구현할 인터페이스
            new UppercaseHandler(new HelloTarget())     // 부가기능과 위임 코드를 담은 InvocationHandler
        );

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO, TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI, TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU, TOBY");   
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice(new UppercaseAdvice());

        Hello proxiedHello = (Hello)pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO, TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI, TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("THANK YOU, TOBY");           
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        // 메소드 이름을 비교해서 대상을 선정하는 알고리즘을 제공하는 포인트컷 생성
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello)pfBean.getObject();

        assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO, TOBY");
        assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI, TOBY");
        assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank you, Toby");    
    }

    static class UppercaseAdvice implements MethodInterceptor {
        @SuppressWarnings("null")
        @Override
        @Nullable
        public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
            /*
             * 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없음
             * MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문
             */
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }
    }

    @Test
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return new ClassFilter() {
                    public boolean matches(Class<?> clazz) {
                        return clazz.getSimpleName().startsWith("HelloT");
                    }
                };
            }
        };

        classMethodPointcut.setMappedName("sayH*");

        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {};
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {};
        checkAdviced(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdviced(Object target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello)pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO, TOBY");
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI, TOBY");
            assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank you, Toby");   
        }
        else {
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("Hello, Toby");
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("Hi, Toby");
            assertThat(proxiedHello.sayThankYou("Toby")).isEqualTo("Thank you, Toby");   
        }
    }
}

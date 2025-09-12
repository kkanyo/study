package tobyspring.learningtest.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {
    Object target;

    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 타깃으로 위임.
        // 인터페이스의 메소드 호출에 모두 적용된다.
        Object ret = method.invoke(target, args);
        
        if (ret instanceof String) {
            return ( (String)ret ).toUpperCase();   // 부가기능 제공
        }
        else {
            return ret;
        }
    }
}

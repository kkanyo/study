package tobyspring.learningtest.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
    String text;

    public void setText(String text) {
        this.text = text;
    }

    // 실제 빈으로 사용될 오브젝트를 직접 생성한다.
    // 코드를 이용하기 때문에 복잡한 방식의 오브젝트 생성과 초기화 작업도 가능하다.
    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }
    
    // 매번 요청할 때마다 새로운 오브젝트를 만드므로 false로 설정한다.
    // 팩토리 빈의 동장방식에 대한 설정이고,
    // 만들어진 빈 오브젝트는 싱글톤으로 스프링이 관리해줄 수 있다.
    @Override
    public boolean isSingleton() {
        return false;
    }
}

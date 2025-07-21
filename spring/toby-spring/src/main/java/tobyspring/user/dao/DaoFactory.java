package tobyspring.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 자바 코드의 탈을 쓰고 있지만, 사실상 XML과 같은 스프링 전용 설정정보
// 애플리케이션 컨텍스트(application context) 또는 빈 팩토리(bean factory)가 사용할 설정 정보라는 표시
@Configuration
public class DaoFactory {
    // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
    @Bean
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}

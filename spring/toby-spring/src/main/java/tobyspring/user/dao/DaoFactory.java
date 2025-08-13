package tobyspring.user.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

// 자바 코드의 탈을 쓰고 있지만, 사실상 XML과 같은 스프링 전용 설정정보
// 애플리케이션 컨텍스트(application context) 또는 빈 팩토리(bean factory)가 사용할 설정 정보라는 표시
@Deprecated
@Configuration
public class DaoFactory {
    // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        // userDao.setConnectionMaker(connectionMaker());
        // userDao.setDataSource(dataSource());
        userDao.setJdbcTemplate(dataSource());
        return userDao;
    
    }

    // @Bean
    // public ConnectionMaker connectionMaker() {
    //     return new DConnectionMaker();
    // }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/toby-spring");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        return dataSource;
    }
}

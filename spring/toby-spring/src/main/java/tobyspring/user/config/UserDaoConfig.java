package tobyspring.user.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tobyspring.user.dao.UserDao;
import tobyspring.user.dao.UserDaoJdbc;
import tobyspring.user.sql.SqlService;

@Configuration
public class UserDaoConfig {

    @Bean
    UserDao userDao(DataSource dataSource, SqlService sqlService) {
        return new UserDaoJdbc(dataSource, sqlService);
    }
}

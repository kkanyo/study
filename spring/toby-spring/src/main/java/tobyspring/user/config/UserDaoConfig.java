package tobyspring.user.config;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tobyspring.user.dao.UserDao;
import tobyspring.user.dao.UserDaoJdbc;
import tobyspring.user.sql.DefautlSqlService;
import tobyspring.user.sql.SqlService;

@Configuration
public class UserDaoConfig {

    @Bean
    UserDao userDao(DataSource dataSource, SqlService sqlService) {
        return new UserDaoJdbc(dataSource, sqlService);
    }

    @Bean
    @ConditionalOnMissingBean(SqlService.class)
    DefautlSqlService defautlSqlService() {
        return new DefautlSqlService();
    }
}

package tobyspring.user.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import tobyspring.user.dao.UserDao;
import tobyspring.user.dao.UserDaoJdbc;
import tobyspring.user.sql.SqlService;

@Configuration
@ComponentScan(basePackageClasses = {UserDao.class, SqlService.class})
public class UserDaoConfig {
    @Bean
    SimpleDriverDataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mysql://localhost/toby-spring");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        return dataSource;
    }

    @Bean
    UserDao userDao(DataSource dataSource, SqlService sqlService) {
        return new UserDaoJdbc(dataSource, sqlService);
    }

    @Bean
    DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

package tobyspring.user.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import tobyspring.user.service.DummyMailSender;
import tobyspring.user.service.UserLevelUpgradeNormal;
import tobyspring.user.service.UserLevelUpgradePolicy;

@Configuration
@ComponentScan(basePackages = "tobyspring.user.*")
/*
 * 트랜잭션 어노테이션의 검색 순서는
 * 구현 클래스 메소드 > 슈퍼 클래스 메소드 > 인터페이스 메소드 >
 * 구현 클래스 > 슈퍼 클래스 > 인터페이스이다.
 */
@EnableTransactionManagement
public class UserServiceConfig {
    @Bean
    @Primary
    UserLevelUpgradePolicy userLevelUpgradePolicy() {
        return new UserLevelUpgradeNormal();
    }

    @Bean
    MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
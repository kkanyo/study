package tobyspring.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;

import tobyspring.user.service.DummyMailSender;
import tobyspring.user.service.UserLevelUpgradeNormal;
import tobyspring.user.service.UserLevelUpgradePolicy;

@Configuration
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
}
package tobyspring.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import tobyspring.user.UserServiceTest;
import tobyspring.user.UserServiceTest.TestUserService;
import tobyspring.user.dao.UserDao;
import tobyspring.user.service.UserLevelUpgradePolicy;

@Configuration
public class UserServiceTestConfig {
    @Bean("TestUserService")
    public TestUserService testUserService(UserDao userDao, MailSender mailSender) {
        UserLevelUpgradePolicy upgradePolicy = new UserServiceTest.TestUserLevelUpgrade("madnite1");

        return new TestUserService(
            userDao,
            mailSender,
            upgradePolicy
        );
    }
}

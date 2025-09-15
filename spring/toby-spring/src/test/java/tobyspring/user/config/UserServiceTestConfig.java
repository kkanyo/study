package tobyspring.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;

import tobyspring.user.UserServiceTest;
import tobyspring.user.dao.UserDao;
import tobyspring.user.service.UserLevelUpgradePolicy;
import tobyspring.user.service.UserService;
import tobyspring.user.service.UserServiceImpl;

@Configuration
public class UserServiceTestConfig {
    @Bean("TestUserService")
    public UserService testUserService(UserDao userDao, MailSender mailSender) {
        UserLevelUpgradePolicy upgradePolicy = new UserServiceTest.TestUserLevelUpgrade("madnite1");

        return new UserServiceImpl(
            userDao,
            mailSender,
            upgradePolicy
        );
    }
}

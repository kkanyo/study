package tobyspring.user.config;

import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import tobyspring.learningtest.jdk.proxy.NameMatchClassMethodPointcut;
import tobyspring.user.service.DummyMailSender;
import tobyspring.user.service.TransactionAdvice;
import tobyspring.user.service.UserLevelUpgradeNormal;
import tobyspring.user.service.UserLevelUpgradePolicy;

@Configuration
@ComponentScan(basePackages = "tobyspring.user.*")
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
    TransactionAdvice transactionAdvice(PlatformTransactionManager transactionManager) {
        return new TransactionAdvice(transactionManager);
    }

    @Bean
    NameMatchClassMethodPointcut transactionPointcut() {
        NameMatchClassMethodPointcut pointcut = new NameMatchClassMethodPointcut();
        pointcut.setMappedClassName("*ServiceImpl");
        pointcut.setMappedNames(new String[] { "upgrade*" } );
        
        return pointcut;
    }

    @Bean
    DefaultPointcutAdvisor transactionAdvisor(PlatformTransactionManager transactionManager) {
        return new DefaultPointcutAdvisor(
            transactionPointcut(),
            transactionAdvice(transactionManager));
    }

    @Bean
    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        return new DefaultAdvisorAutoProxyCreator();
    }
}
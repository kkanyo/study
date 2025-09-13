package tobyspring.user.config;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import tobyspring.user.service.DummyMailSender;
import tobyspring.user.service.TransactionAdvice;
import tobyspring.user.service.UserLevelUpgradeNormal;
import tobyspring.user.service.UserLevelUpgradePolicy;
import tobyspring.user.service.UserServiceImpl;

@Configuration
@ComponentScan(basePackages = "tobyspring.user.*")
@RequiredArgsConstructor
public class UserServiceConfig {
    private final UserServiceImpl userServiceImpl;

    @Bean
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
    NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
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
    ProxyFactoryBean userService() throws ClassNotFoundException {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(userServiceImpl);
        pfBean.setInterceptorNames("transactionAdvisor");

        return pfBean;
    }
}
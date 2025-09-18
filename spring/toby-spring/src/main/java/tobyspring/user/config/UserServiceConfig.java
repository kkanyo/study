package tobyspring.user.config;

import java.util.Properties;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import tobyspring.user.service.DummyMailSender;
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
    TransactionInterceptor transactionAdvice(PlatformTransactionManager transactionManager) {
        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager);

        Properties attributes = new Properties();

        attributes.setProperty("get*", "PROPAGATION_REQUIRED, readOnly");
        attributes.setProperty("*", "PROPAGATION_REQUIRED");
        
        transactionInterceptor.setTransactionAttributes(attributes);

        return transactionInterceptor;
    }

    @Bean
    AspectJExpressionPointcut transactionPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*Service)");

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
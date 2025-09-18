package tobyspring.learningtest.jdk.proxy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@RequiredArgsConstructor
public class TransactionAdvice implements MethodInterceptor {
    private final PlatformTransactionManager transactionManager;

    @Override
    @Nullable
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        TransactionStatus status = this.transactionManager.getTransaction(
            new DefaultTransactionDefinition());

        try {
            Object ret = invocation.proceed();
            
            this.transactionManager.commit(status);

            return ret;
        } catch (RuntimeException e) {
            // 스프링의 MethodInvocation을 통한 타깃 호출은 예외가 포장되지 않고 타깃에서 보낸 그대로 전달됨
            this.transactionManager.rollback(status);
            throw e;
        }
    }
}

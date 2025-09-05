package tobyspring.user.service;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import tobyspring.user.domain.User;

public class UserServiceTx implements UserService {
    // DataSource dataSource;
    UserService userService;
    PlatformTransactionManager transactionManager;  // 스프링이 제공하는 트랜잭션 경계설정을 위한 추상 인터페이스

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void add(User user) {
        this.userService.add(user);    
    }

    public void upgradeLevels() {
        // // 트랜잭션 동기화 관리자를 이용해 동기화 작업을 초기화
        // TransactionSynchronizationManager.initSynchronization();
        // // DB 커넥션을 생성하고 트랜잭션을 시작한다.
        // // 이후의 DAO 작업은 모두 여기서 시작한 트랜잭션 안에서 진행된다.
        // // DataSourceUtils의 getConnection() 메소드는 Connection 오브젝트를 생성해줄 뿐만 아니라
        // // 트랜잭션 동기화에 사용하도록 저장소에 바인딩해준다.
        // Connection c = DataSourceUtils.getConnection(dataSource);
        // c.setAutoCommit(false);

        TransactionStatus status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();

            // c.commit();
            this.transactionManager.commit(status);  // PlatformTrasactionManager가 내부적으로 커넥션 반환을 처리
        }
        catch (Exception e) {
            // c.rollback();
            this.transactionManager.rollback(status);
            throw e;
        }
        // finally {
        //     // 스프링 유틸리티 메소드를 이용해 DB 커넥션을 안전하게 닫는다.
        //     DataSourceUtils.releaseConnection(c, dataSource);
        //     // 동기화 작업 종료 및 정리
        //     TransactionSynchronizationManager.unbindResource(dataSource);
        //     TransactionSynchronizationManager.clearSynchronization();
        // }
    }
}

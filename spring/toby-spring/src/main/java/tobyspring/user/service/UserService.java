package tobyspring.user.service;

import java.util.List;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

public class UserService {
    UserDao userDao;
    UserLevelUpgradePolicy userLevelUpgradePolicy;
    // private DataSource dataSource;
    private PlatformTransactionManager transactionManager;  // 스프링이 제공하는 트랜잭션 경계설정을 위한 추상 인터페이스

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    public void setUserLevelUpgradePolicy(UserLevelUpgradePolicy userLevelUpgradePolicy) {
        this.userLevelUpgradePolicy = userLevelUpgradePolicy;
    }

    // public void setDataSource(DataSource dataSource) {
    //     this.dataSource = dataSource;
    // }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }

        userDao.add(user);
    }

    /*
     * 조건이 변경하게 되면 코드를 전반적으로 수정해야 하는 문제가 발생
     * 레벨을 확인하는 작업과 레벨을 변경시키는 작업을 분리시킬 필요가 있음
     */
    @Deprecated
    public void upgradeLevelsSimple() {
        List<User> users = userDao.getAll();

        for (User user : users) {
            Boolean changed = null;

            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            }
            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            }
            else if (user.getLevel() == Level.GOLD) {
                changed = false;
            }
            else {
                changed = false;
            }

            if (changed) {
                userDao.update(user);
            }
        }
    }

    public void upgradeLevels() throws Exception {
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
            List<User> users = userDao.getAll();
    
            for (User user: users) {
                if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                    userLevelUpgradePolicy.upgradeLevel(user);
                    // JdbcTemplate의 메소드에서는 직접 DB 커넥션을 만드는 대신
                    // 트랜잭션 동기화 저장소에 들어 있는 DB 커넥션을 가져와서 사용한다.
                    userDao.update(user);
                }
            }

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

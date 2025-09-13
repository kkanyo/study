package tobyspring.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static tobyspring.user.service.UserLevelUpgradeNormal.MIN_LOGCOUNT_FOR_SILVER;
import static tobyspring.user.service.UserLevelUpgradeNormal.MIN_RECOMMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import tobyspring.user.config.UserDaoConfig;
import tobyspring.user.config.UserServiceConfig;
import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;
import tobyspring.user.service.UserLevelUpgradeNormal;
import tobyspring.user.service.UserLevelUpgradePolicy;
import tobyspring.user.service.UserService;
import tobyspring.user.service.UserServiceImpl;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserDaoConfig.class, UserServiceConfig.class})
public class UserServiceTest {
    static class TestUserLevelUpgrade extends UserLevelUpgradeNormal {
        private String id;

        private TestUserLevelUpgrade(String id) {
            this.id = id;
        }

        public void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }

            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    private long startTime;

    @Autowired UserDao userDao;
    @Autowired DataSource dataSource;
    @Autowired ApplicationContext context;
    @Autowired UserService userService;
    @Autowired PlatformTransactionManager transactionManager;
    @Autowired MailSender mailSender;
    @Autowired UserServiceImpl userServiceImpl;
    
    List<User> users;

    @BeforeEach
    public void setUp() {
        users = Arrays.asList(
            new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "tjrhks12@naver.com"),
            new User("joytouch", "김영성", "p2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "tjrhks12@naver.com"),
            new User("erwins", "신승한", "p3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "tjrhks12@naver.com"),
            new User("madnite1", "이상호", "p4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "tjrhks12@naver.com"),
            new User("green", "오민규", "p5", Level.GOLD, 100, Integer.MAX_VALUE, "tjrhks12@naver.coms")
        );

        startTime = System.currentTimeMillis();
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        long endTime = System.currentTimeMillis();
        // 또는 endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println(testInfo.getDisplayName() + " executed in " + duration + " ms");
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);
        
        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutUserRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutUserRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    @DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트라는 것을 알려준다.
    public void upgradeLevels() throws Exception {
        // 고립된 테스트에서는 테스트 대상 오브젝트를 직접 생성하면 된다.
        // 컨테이너에서 가져온 UserService 오브젝트는
        // DI를 통해서 많은 의존 오브젝트와 서비스, 외부 환경에 의존하고 있기 때문이다.
        UserServiceImpl userServiceImpl = new UserServiceImpl();
        userServiceImpl.setUserLevelUpgradePolicy(new UserLevelUpgradeNormal());
        
        // 목 오브젝트로 만든 UserDao를 직접 DI 해준다.
        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);
        
        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel()).isEqualTo(Level.GOLD);

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0]).isEqualTo(users.get(1).getEmail());   
        assertThat(mailMessages.get(1).getTo()[0]).isEqualTo(users.get(3).getEmail());   
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        }
        else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    @Test
    @DirtiesContext
    public void upgradeAllOrNothing() throws Exception {
        UserLevelUpgradePolicy testUserLevelUpgradePolicy = new TestUserLevelUpgrade(users.get(3).getId());
        userServiceImpl.setUserLevelUpgradePolicy(testUserLevelUpgradePolicy);
        
        ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(userServiceImpl);       

        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll(); 

        for (User user : users) {
            userDao.add(user);
        }

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }
        catch (TestUserServiceException e) {}

        checkLevelUpgraded(users.get(1), false);
    }
}
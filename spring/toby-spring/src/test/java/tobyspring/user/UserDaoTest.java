package tobyspring.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.User;

public class UserDaoTest {
    ApplicationContext context = null;
    UserDao dao = null;

    User user1 = null;
    User user2 = null;
    User user3 = null;

    @BeforeEach
    public void setUp() {
        context = new GenericXmlApplicationContext("applicationContext.xml");
        dao = context.getBean("userDao", UserDao.class);

        user1 = new User();
        user1.setId("kkanyo");
        user1.setName("서관영");
        user1.setPassword("cheshire");

        user2 = new User();
        user2.setId("cv");
        user2.setName("carrier");
        user2.setPassword("formidable");

        user3 = new User();
        user3.setId("dd");
        user3.setName("halford");
        user3.setPassword("destroyer");
    }
    
    @Test
    public void getAll() throws SQLException {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isEqualTo(0);

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user2, users2.get(0));
        checkSameUser(user1, users2.get(1));
    
        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user2, users3.get(0));
        checkSameUser(user3, users3.get(1));
        checkSameUser(user1, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
    }

}

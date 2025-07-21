package tobyspring.user;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import tobyspring.user.dao.DaoFactory;
import tobyspring.user.dao.UserDao;
import tobyspring.user.domain.User;

public class UserDaoTest {
        public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // UserDao와 ConnectionMaker 구현 클래스와의 런타임 오브젝트 의존 관계를 설정하는 책임을 담당해야 한다.
        // ConnectionMaker connectionMaker = new DConnectionMaker();
        
        // UserDao dao = new UserDao(connectionMaker);

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("kkanyo");
        user.setName("서관영");
        user.setPassword("password");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + " 조회 성공");
    }
}

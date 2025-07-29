package tobyspring.user.dao;

import java.sql.SQLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import tobyspring.user.domain.User;

public class UserDaoTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        objectEqualTest();
    }

    static public void dbConnectionTest() throws ClassNotFoundException, SQLException {
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

    static public void objectEqualTest() {
        DaoFactory daoFactory = new DaoFactory();
        UserDao dao1 = daoFactory.userDao();
        UserDao dao2 = daoFactory.userDao();

        System.out.println(dao1);
        System.out.println(dao2);
        System.out.println(dao1 == dao2); // false, 매번 새로운 오브젝트를 생성한다.

        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println(dao3);
        System.out.println(dao4);
        System.out.println(dao3 == dao4); // true, 싱글톤으로 관리되므로 같은 오브젝트를 반환한다. 
    }
}

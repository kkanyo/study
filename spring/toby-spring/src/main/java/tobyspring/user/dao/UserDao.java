package tobyspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tobyspring.user.domain.User;

public class UserDao {
    // 상속으로 분리가 가능하지만, 이중 상속이 불가능하여 이후 확장과 관련하여 단점을 가진다.
    // public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

    private ConnectionMaker connectionMaker;    // 초기에 설정하면 사용 중에는 바뀌지 않는 읽기전용 인스턴스 변수
    // 매번 새로운 값으로 바뀌는 정보를 담은 인스턴스 변수 (멀티 스레드 환경에서 문제가 될 수 있다.)

    // 구체적인 클래스를 직접 사용하여 의존성을 가지게 된다.
    // public UserDao() {
    //     connectionMaker = new DConnectionMaker();
    // }

    // public UserDao(ConnectionMaker connectionMaker) {
    //     this.connectionMaker = connectionMaker;
    // }

    public void setConnectionMaker(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    public void add(User user) throws ClassNotFoundException, SQLException {
        // 코드 중복 및 생산성 문제가 발생하므로 좋지 않은 방식이다.
        // Class.forName("com.mysql.cj.jdbc.Driver");
        // Connection c = DriverManager.getConnection(
        //     "jdbc:mysql://localhost/toby_spring", "root", "password");

        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement(
            "INSERT INTO user(id, name, password) VALUES(?, ?, ?)"
        );
        ps.setString(1, user.getId());
        ps.setString(2, user.getName());
        ps.setString(3, user.getPassword());

        ps.executeUpdate();
        
        ps.close();
        c.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection c = connectionMaker.makeConnection();

        PreparedStatement ps = c.prepareStatement(
            "SELECT * FROM user WHERE id = ?"
        );
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        c.close();

        return user;
    }

}

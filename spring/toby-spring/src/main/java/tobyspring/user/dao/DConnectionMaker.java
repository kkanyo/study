package tobyspring.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// 독립시킨 DB 연결 기능
// 그러나 UserDao가 SimpleConnectionMaker에 종속적이기 때문에 확장성이 떨어진다.
// 따라서 인터페이스를 사용하여 의존성을 분리한다.
@Deprecated
public class DConnectionMaker implements ConnectionMaker{
    public Connection makeConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
            "jdbc:mysql://localhost/toby-spring", "root", "password");
    }
}

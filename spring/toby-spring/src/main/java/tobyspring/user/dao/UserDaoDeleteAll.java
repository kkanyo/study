package tobyspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * DAO 로직마다 상속을 통해 새로운 클래스를 만들어야 한다는 점과
 * 확장구조가 이미 클래스를 설계하는 시점에서 고정되어 버린다는 점이 단점이다.
 */
@Deprecated
public class UserDaoDeleteAll extends UserDaoJdbc {
    protected PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("delete from user");
    }
}

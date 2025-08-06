package tobyspring.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import tobyspring.user.domain.User;

public class UserDao {
    // 상속으로 분리가 가능하지만, 이중 상속이 불가능하여 이후 확장과 관련하여 단점을 가진다.
    // public abstract Connection getConnection() throws ClassNotFoundException, SQLException;

    // private ConnectionMaker connectionMaker;    // 초기에 설정하면 사용 중에는 바뀌지 않는 읽기전용 인스턴스 변수

    // 구체적인 클래스를 직접 사용하여 의존성을 가지게 된다.
    // public UserDao() {
    //     connectionMaker = new DConnectionMaker();
    // }

    // public UserDao(ConnectionMaker connectionMaker) {
    //     this.connectionMaker = connectionMaker;
    // }

    // public void setConnectionMaker(ConnectionMaker connectionMaker) {
    //     this.connectionMaker = connectionMaker;
    // }

    // private DataSource dataSource;
    // private JdbcContext jdbcContext;
    
    // public void setDataSource(DataSource dataSource) {
        //     /*
        //      * 굳이 인터페이스를 사용하지 않아도 될 만큼 긴밀한 관계를 갖는 DAO 클래스와 JdbcContext를
        //      * 어색하게 따로 빈으로 분리하지 않고 내부에서 직접 만들어 사용하면서
        //      * 다른 오브젝트에 대한 DI를 적용할 수 있다는 장점이 있다.
        //      * 하지만 JdbcContext를 여러 오브젝트가 사용하더라도 싱글톤으로 만들 수 없고,
        //      * DI 작업을 위한 부가적인 코드가 필요하다는 단점도 있다.
        //      */
        
        //     // jdbcContext 생성(IoC)
        //     this.jdbcContext = new JdbcContext();
        
        //     // DI
        //     jdbcContext.setDataSource(dataSource);
        
        //     this.dataSource = dataSource;
        // }
        
    /*
     * 인터페이스를 사용하지 않는 클래스와의 의존관계이지만
     * 스프링의 DI를 이용하기 위해 빈으로 등록해서 사용하는 방법은
     * 오브젝트 사이의 실제 의존관계가 설정파일에 명확하게 드러난다는 장점이 있다.
     * 하지만 DI의 근본적인 원칙에 부합하지 않는 구체적인 클래스와의 관계가
     * 설정에 직접 노출된다는 단점이 있다.
     */
    // jdbcContext를 DI로 주입받도록 만든다.
    // public void setJdbcContext(JdbcContext jdbcContext) {
    //     this.jdbcContext = jdbcContext;
    // }

    // // DI 적용, 클라이언트가 컨텍스트를 호출할 때 전략 파라미터를 넘겨준다.
    // public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
    //     Connection c = null;
    //     PreparedStatement ps = null;

    //     try {
    //         c = dataSource.getConnection();

    //         ps = stmt.makeStatement(c);

    //         ps.executeUpdate();
    //     } catch (SQLException e) {
    //         throw e;
    //     } finally {
    //         if (ps != null) {
    //             try {
    //                 ps.close();
    //             } catch (SQLException e) {}
    //         }

    //         if (c != null) {
    //             try {
    //                 c.close();
    //             } catch (SQLException e) {}
    //         }
    //     }
    // }

    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<User> userMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            // RoeMapper가 호출되는 시점에서 ResultSet은 첫 번째 row를 가리키고 있으므로 다시 rs.next()를 호출할 필요가 없다.
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    public void add(final User user) throws SQLException {
        // // 코드 중복 및 생산성 문제가 발생하므로 좋지 않은 방식이다.
        // Class.forName("com.mysql.cj.jdbc.Driver");
        // Connection c = DriverManager.getConnection(
        //     "jdbc:mysql://localhost/toby_spring", "root", "password");

        // // 로컬 클래스
        // // 내부 클래스에서 외부의 변수를 사용할 때 외부 변수는 반드시 final로 선언해줘야 한다.
        // class AddStatement implements StatementStrategy {
        //     @Override
        //     public PreparedStatement makeStatement(Connection c) throws SQLException {
        //         PreparedStatement ps = c.prepareStatement("INSERT INTO user(id, name, password) VALUES (?, ?, ?)");
                
        //         ps.setString(1, user.getId());
        //         ps.setString(2, user.getName());
        //         ps.setString(3, user.getPassword());

        //         return ps;
        //     }
        // }

        // StatementStrategy st = new AddStatement();

        // // 익명 클래스
        // jdbcContextWithStatementStrategy(
        // this.jdbcContext.workWithStatementStrategy(
        //     new StatementStrategy() {
        //         public PreparedStatement makeStatement(Connection c) throws SQLException {
        //             PreparedStatement ps = c.prepareStatement("INSERT INTO user(id, name, password) VALUES (?, ?, ?)");

        //             ps.setString(1, user.getId());
        //             ps.setString(2, user.getName());
        //             ps.setString(3, user.getPassword());

        //             return ps;
        //         }
        //     }
        // );

        // this.jdbcContext.executeSql("INSERT INTO user(id, name, password) VALUES (?, ?, ?)", 
        //     user.getId(), 
        //     user.getName(), 
        //     user.getPassword());

        this.jdbcTemplate.update("INSERT INTO user(id, name, password) VALUES (?, ?, ?)",
            user.getId(),
            user.getName(),
            user.getPassword());
    }

    public User get(String id) throws SQLException {
        // 실행해서 받은 row의 개수가 하나가 아니라면 예외를 던지로고 처리되어 있다. (EmptyResultDataAccessException)
        return this.jdbcTemplate.queryForObject("SELECT * FROM user WHERE id = ?",
            new Object[] {id},
            new int[] {java.sql.Types.CHAR},
            this.userMapper);
    }

    public List<User> getAll() throws SQLException {
        return this.jdbcTemplate.query("SELECT * FROM user ORDER BY id",
            this.userMapper);
    }

    // 반복되는 코드가 많아진다.
    // public void deleteAll() throws SQLException {
    //     Connection c = null;
    //     PreparedStatement ps = null;

    //     try {
    //         c = dataSource.getConnection();
            
    //         // ps = c.prepareStatement("delete from user");    // 변하는 부분
    //         // ps = makeStatement(c);
    //         StatementStrategy strategy = new DeleteAllStatement();  // 특정 구현 클래스과 관계가 결정되어 있다는 점이 문제
    //         ps = strategy.makeStatement(c);
            
    //         ps.executeUpdate();
    //     } catch (SQLException e) {
    //         throw e;
    //     } finally {
    //         if (ps != null) {
    //             // try/catch를 사용하지 않으면 ps.close()에서 예외가 발생했을 때 c.close()가 호출되지 않는 문제가 발생한다.
    //             try {
    //                 ps.close();
    //             } catch (SQLException e) {}
    //         }

    //         if (c != null) {
    //             try {
    //                 c.close();
    //             } catch (SQLException e) {}
    //         }
    //     }
    // }

    public void deleteAll() throws SQLException {
        // StatementStrategy st = new DeleteAllStatement();
        // jdbcContextWithStatementStrategy(st);

        // jdbcContextWithStatementStrategy(
        // this.jdbcContext.workWithStatementStrategy(
        //     new StatementStrategy() {
        //         public PreparedStatement makeStatement(Connection c) throws SQLException {
        //             PreparedStatement ps = c.prepareStatement("DELETE FROM user");
                    
        //             return ps;
        //         }
        //     }
        // );

        // this.jdbcContext.executeSql("DELETE FROM user");

        // this.jdbcTemplate.update(
        //     new PreparedStatementCreator() {
        //         public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        //             return con.prepareStatement("DELETE FROM user");
        //         }
        //     }
        // );

        this.jdbcTemplate.update("DELETE FROM user");
    }

    // 분리시킨 메소드를 다른 곳에서 재사용할 수 없다.
    // private PreparedStatement makeStatement(Connection c) throws SQLException{
    //     return c.prepareStatement("delete from user");
    // }

    public int getCount() throws SQLException {
        // return jdbcTemplate.query(new PreparedStatementCreator() {
        //     public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        //         return con.prepareStatement("SELECT count(*) FROM user");
        //     }
        // }, new ResultSetExtractor<Integer>() {
        //     public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
        //         rs.next();

        //         return rs.getInt(1);
        //     }
        // });

        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM user", Integer.class);
    }
}
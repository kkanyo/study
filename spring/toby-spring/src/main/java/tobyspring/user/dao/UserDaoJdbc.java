package tobyspring.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import tobyspring.user.domain.Level;
import tobyspring.user.domain.User;

public class UserDaoJdbc implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<User> userMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            // RoeMapper가 호출되는 시점에서 ResultSet은 첫 번째 row를 가리키고 있으므로 다시 rs.next()를 호출할 필요가 없다.
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            user.setLevel(Level.valueOf(rs.getInt("level")));
            user.setLogin(rs.getInt("login"));
            user.setRecommend(rs.getInt("recommend"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    };

    public void add(final User user) {
        this.jdbcTemplate.update("INSERT INTO user(id, name, password, level, login, recommend, email) VALUES (?, ?, ?, ?, ?, ?, ?)",
            user.getId(),
            user.getName(),
            user.getPassword(),
            user.getLevel().intValue(),
            user.getLogin(),
            user.getRecommend(),
            user.getEmail()
        );
    }

    public User get(String id) {
        // 실행해서 받은 row의 개수가 하나가 아니라면 예외를 던지로고 처리되어 있다. (EmptyResultDataAccessException)
        return this.jdbcTemplate.queryForObject("SELECT * FROM user WHERE id = ?",
            new Object[] {id},
            new int[] {java.sql.Types.CHAR},
            this.userMapper);
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM user ORDER BY id",
            this.userMapper);
    }

    public void deleteAll() {
        this.jdbcTemplate.update("DELETE FROM user");
    }

    public int getCount() {
        return this.jdbcTemplate.queryForObject("SELECT count(*) FROM user", Integer.class);
    }

    public void update(User user) {
        this.jdbcTemplate.update("UPDATE user SET name=?, password=?, level=?, login=?, recommend=?, email=? WHERE id=?",
            user.getName(),
            user.getPassword(),
            user.getLevel().intValue(),
            user.getLogin(),
            user.getRecommend(),
            user.getEmail(),
            user.getId()
        );
    }
}
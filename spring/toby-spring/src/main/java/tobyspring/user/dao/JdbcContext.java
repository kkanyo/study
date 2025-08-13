package tobyspring.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

@Deprecated
public class JdbcContext {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }   

    public void workWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = stmt.makeStatement(c);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {}
            }

            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {}
            }
        }
    }

    public void executeSql(final String query) throws SQLException {
        workWithStatementStrategy(
            new StatementStrategy() {
                public PreparedStatement makeStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement(query);
                    
                    return ps;
                }
            }
        );
    }

    public void executeSql(final String query, Object... args) throws SQLException {
        workWithStatementStrategy(
            new StatementStrategy() {
                public PreparedStatement makeStatement(Connection c) throws SQLException {
                    PreparedStatement ps = c.prepareStatement(query);

                    for (int i = 1; i <= args.length; i++) {
                        if (args[i - 1] == null) {
                            ps.setNull(i, java.sql.Types.NULL);
                        } else {
                            ps.setObject(i, args[i - 1]);
                        }
                    }

                    return ps;
                }
            }
        );
    }
}

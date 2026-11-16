package tobyspring.user.sql;

import org.springframework.stereotype.Component;

@Component
public interface SqlReader {
    void read(SqlRegistry sqlRegistry);
}

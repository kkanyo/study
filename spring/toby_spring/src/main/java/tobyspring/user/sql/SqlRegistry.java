package tobyspring.user.sql;

import org.springframework.stereotype.Component;

@Component
public interface SqlRegistry {

    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException;
}

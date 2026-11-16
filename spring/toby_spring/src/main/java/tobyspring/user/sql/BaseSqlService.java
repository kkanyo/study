package tobyspring.user.sql;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class BaseSqlService implements SqlService {

    protected final SqlReader sqlReader;
    protected final SqlRegistry sqlRegistry;
    
    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        }
        catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }
}
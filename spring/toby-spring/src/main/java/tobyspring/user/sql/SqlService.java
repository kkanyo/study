package tobyspring.user.sql;

public interface SqlService {
    
    String getSql(String key) throws SqlRetrievalFailureException;
}

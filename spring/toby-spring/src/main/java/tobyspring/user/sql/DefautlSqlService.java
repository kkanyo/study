package tobyspring.user.sql;

public class DefautlSqlService extends BaseSqlService {
    
    public DefautlSqlService() {
        super(new JaxbXmlSqlReader(), new HashMapSqlRegistry());
    }
}

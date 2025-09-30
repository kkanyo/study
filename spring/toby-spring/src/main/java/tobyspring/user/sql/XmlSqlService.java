package tobyspring.user.sql;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import lombok.RequiredArgsConstructor;
import tobyspring.user.properties.SqlProperties;
import tobyspring.user.sql.jaxb.SqlType;
import tobyspring.user.sql.jaxb.Sqlmap;

@RequiredArgsConstructor
@Service
public class XmlSqlService implements SqlService {

    private final SqlProperties sqlProperties;
    private Map<String, String> sqlMap = new HashMap<String, String>();

    @PostConstruct  // Specify as initialize method of bean
    public void loadSql() {
        String contextPath = Sqlmap.class.getPackage().getName();

        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = getClass().getClassLoader().getResourceAsStream(sqlProperties.fileName());
            Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlMap.put(sql.getKey(), sql.getValue());
            }
        } catch (JAXBException e) {
            // Since `JAXBException` is unrecoverable, 
            // wrap it in `RuntimeException` to avoid unnecessory 'throws' declarations.
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);

        if (sql == null) {
            throw new SqlRetrievalFailureException("cannot find SQL with " + key);
        }
        else {
            return sql;
        }
    }
}
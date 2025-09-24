package learningtest.jdk.jaxb;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import tobyspring.user.sql.jaxb.SqlType;
import tobyspring.user.sql.jaxb.Sqlmap;

public class JaxbTest {
    
    @Test
    public void readSqlmap() throws JAXBException, IOException {
        String contextPath = Sqlmap.class.getPackage().getName();
        JAXBContext context = JAXBContext.newInstance(contextPath); // 바인딩용 클래스들 위치를 가지고 JAXB 컨텍스트 생성

        Unmarshaller unmarshaller = context.createUnmarshaller();

        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(
            getClass().getResourceAsStream("sqlmap.xml")
        );

        List<SqlType> sqlList = sqlmap.getSql();
        
        assertThat(sqlList.size()).isEqualTo(3);
        assertThat(sqlList.get(0).getKey()).isEqualTo("add");
        assertThat(sqlList.get(0).getValue()).isEqualTo("insert");
        assertThat(sqlList.get(1).getKey()).isEqualTo("get");
        assertThat(sqlList.get(1).getValue()).isEqualTo("select");
        assertThat(sqlList.get(2).getKey()).isEqualTo("delete");
        assertThat(sqlList.get(2).getValue()).isEqualTo("delete");
    }
}

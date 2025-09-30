package tobyspring.user.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sql-mapper")
public record SqlProperties(String fileName) {

}
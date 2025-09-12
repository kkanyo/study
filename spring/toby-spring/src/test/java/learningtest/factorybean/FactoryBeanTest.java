package learningtest.factorybean;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import tobyspring.learningtest.factorybean.Message;
import tobyspring.learningtest.factorybean.MessageFactoryBean;

@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class FactoryBeanTest {

    @Configuration
    static class Config {
        @Bean
        MessageFactoryBean message() {
            MessageFactoryBean message = new MessageFactoryBean();
            message.setText("Factory Bean");

            return message;
        }
    }

    @Autowired
    ApplicationContext context;

    @Test
    public void getMessageFromFactoryBean() {
        System.out.println(context.getBean("message"));
        Object message = context.getBean("message");
        assertThat(message).isInstanceOf(Message.class);
        assertThat(( (Message)message ).getText()).isEqualTo("Factory Bean");
    }

    @Test
    public void getFactoryBean() {
        Object factory = context.getBean("&message");
        assertThat(factory).isInstanceOf(MessageFactoryBean.class);
    }
}

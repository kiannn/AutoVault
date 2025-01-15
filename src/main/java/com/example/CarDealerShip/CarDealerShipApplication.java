package com.example.CarDealerShip;

import jakarta.servlet.MultipartConfigElement;
import javax.sql.DataSource;
import javax.xml.crypto.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
public class CarDealerShipApplication extends SpringBootServletInitializer{

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{CarDealerShipApplication.class, SecurityConfig.class}, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CarDealerShipApplication.class);
    }
    
    @Bean
    public JdbcUserDetailsManager userDetails(DataSource DataSource) {

        JdbcUserDetailsManager JdbcUserDetailsManager = new JdbcUserDetailsManager(DataSource);

        return JdbcUserDetailsManager;
    }

    @Bean
    public PasswordEncoder pe() { 

        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnProperty(name = "nhtsa.base.url")
    @ConditionalOnExpression("#{!'${nhtsa.base.url}'.isBlank()}")
    public WebClient client(@Value("${nhtsa.base.url}") String baseURL){
     
    return WebClient.create(baseURL);
    }
}

package com.example.CarDealerShip;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class CarDealerShipApplication { //extends SpringBootServletInitializer {

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        
//        System.out.println("com.example.CarDealerShip.CarDealerShipApplication.configure()\n");
//        return application.sources(new Class[]{CarDealerShipApplication.class, SecurityConfig.class});//Add more sources (configuration classes and components) to this application.
//    }

    public static void main(String[] args) {
        SpringApplication.run(new Class[]{CarDealerShipApplication.class, SecurityConfig.class}, args);
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
    
    @Bean
    @Qualifier("userClient")
    @Autowired
    public WebClient clientUser(@Value("${user.data.endpoint}") String baseURL){
     
    return WebClient.create(baseURL);
    }
}

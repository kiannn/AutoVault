package com.example.CarDealerShip;

import javax.sql.DataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(auth
                -> auth.requestMatchers("/cars/**",
                                        "/home/**",
                                        "/userpro/**",
                                        "/userpass/**",
                                        "/deletaccount/**",
                                        "/document/**")
                        .authenticated()
                        .anyRequest().permitAll())
                        .formLogin(form -> form.loginPage("/loginpage")
                                               .loginProcessingUrl("/loginprocess")
                                               .defaultSuccessUrl("/cars/home/showallcars", true));
        
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));
        http.headers(a -> a.frameOptions(b -> b.sameOrigin()));
        
        return http.build();
    }

//    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                . url("jdbc:mysql://viaduct.proxy.rlwy.net:49955/railway")
//                .url("jdbc:h2:mem:testdb")
                .build();
    }
}

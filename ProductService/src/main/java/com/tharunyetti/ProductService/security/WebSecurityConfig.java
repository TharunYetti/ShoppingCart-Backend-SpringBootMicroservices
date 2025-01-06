package com.tharunyetti.ProductService.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(
                        authorizeRequest -> authorizeRequest
                                .anyRequest() //allowing every request with authorization
                                .authenticated())
                .oauth2ResourceServer(
                        oauth2-> oauth2.jwt(jwtConfigurer -> {})
                );

        return http.build();
    }
}

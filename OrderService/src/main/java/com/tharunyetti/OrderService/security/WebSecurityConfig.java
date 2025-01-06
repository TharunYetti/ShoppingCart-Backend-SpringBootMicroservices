package com.tharunyetti.OrderService.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception{
//        http
//                .securityMatcher(ServerWebExchangeMatchers.anyExchange())  // Replaces `authorizeExchange()`
//                .authorizeExchange(authorize -> authorize
//                        .anyExchange().authenticated()  // Keeps this part the same
//                )
//                .oauth2Login(Customizer.withDefaults())  // Updated for `oauth2Login()`
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(Customizer.withDefaults())  // Updated for JWT handling
//                );
        http
                .authorizeHttpRequests(authorizeRequest -> authorizeRequest
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(configurer -> configurer
                        .jwt(jwtConfigurer -> {}));

        return http.build();

    }
}

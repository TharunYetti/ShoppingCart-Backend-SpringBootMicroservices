package com.tharunyetti.CloudGateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebFluxSecurity
public class OktaOAuth2WebSecurity {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) throws Exception {

//        http
//                .securityMatcher(ServerWebExchangeMatchers.anyExchange())  // Replaces `authorizeExchange()`
//                .authorizeExchange(authorize -> authorize
//                        .anyExchange().authenticated()  // Keeps this part the same
//                )
//                .oauth2Login(Customizer.withDefaults())  // Updated for `oauth2Login()`
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(Customizer.withDefaults())  // Updated for JWT handling
//                );

//        http
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/**").authenticated() // Secure /*
//                        .anyExchange().permitAll() // Allow other routes
//                )
//                .exceptionHandling(exceptionHandling ->
//                        exceptionHandling.authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/authenticate"))
//                )
//                .oauth2Login(oauth2Login -> oauth2Login
//                        .authenticationSuccessHandler((webFilterExchange, authentication) -> {
//                            // Handle successful authentication here
//                            return webFilterExchange.getExchange().getResponse().setComplete();
//                        })
//                );
//
//        // CSRF configuration (disable if not needed)
//        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        http
                .authorizeExchange(auth-> auth
                    .anyExchange().authenticated())
                .oauth2Login(withDefaults())
                .oauth2ResourceServer(configurer -> configurer
                        .jwt(jwtConfigurer -> {}));

        return http.build();
    }
}

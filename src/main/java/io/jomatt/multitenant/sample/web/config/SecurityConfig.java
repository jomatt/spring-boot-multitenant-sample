package io.jomatt.multitenant.sample.web.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    @Profile("!prod")
    public SecurityFilterChain headerFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authz -> authz
                .anyRequest().permitAll()
        );

        return http.build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain jwtFilterChain(
            HttpSecurity http, AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver)
            throws Exception {

        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(WebMvcConfig.EXCLUDED_PATHS).permitAll()
                .anyRequest().authenticated()
        );
        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationManagerResolver(authenticationManagerResolver)
        );

        return http.build();
    }

}

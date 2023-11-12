package io.jomatt.multitenant.sample.jwt.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("prod")
public class SecurityConfig {

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    @Autowired
    public SecurityConfig(AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) {
        this.authenticationManagerResolver = authenticationManagerResolver;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        // error endpoints
                        .requestMatchers("/error").permitAll()

                        // swagger resources
                        .requestMatchers(
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // authenticate every request
                        .anyRequest().authenticated())

                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationManagerResolver(this.authenticationManagerResolver));

        return http.build();
    }

}

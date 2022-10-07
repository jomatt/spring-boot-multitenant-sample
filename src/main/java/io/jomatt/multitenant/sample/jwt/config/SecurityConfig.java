package io.jomatt.multitenant.sample.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletRequest;

@Configuration
@Profile("production")
public class SecurityConfig {

    private final AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver;

    @Autowired
    public SecurityConfig(AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver) {
        this.authenticationManagerResolver = authenticationManagerResolver;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(a -> a
                        // error endpoints
                        .antMatchers("/error").permitAll()

                        // swagger resources
                        .antMatchers(
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()

                        // authenticate every request
                        .anyRequest().authenticated())

                .oauth2ResourceServer(o -> o
                        .authenticationManagerResolver(this.authenticationManagerResolver));

        return http.build();
    }

}

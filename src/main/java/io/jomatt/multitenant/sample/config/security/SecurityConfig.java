package io.jomatt.multitenant.sample.config.security;

import io.quantics.multitenant.oauth2.config.MultiTenantAuthenticationManagerResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import javax.servlet.http.HttpServletRequest;

public class SecurityConfig {

    private final AuthenticationManagerResolver<HttpServletRequest> tenantResolver;

    public SecurityConfig(MultiTenantAuthenticationManagerResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().and()
                .cors().and()
                .authorizeRequests(a -> a
                        // error endpoints
                        .antMatchers("/error").permitAll()

                        // swagger resources
                        .antMatchers(
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**").permitAll()

                        // authenticate every request
                        .anyRequest().authenticated())

                .oauth2ResourceServer(o -> o
                        .authenticationManagerResolver(this.tenantResolver));

        return http.build();
    }

}

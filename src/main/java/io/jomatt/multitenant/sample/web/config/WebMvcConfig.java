package io.jomatt.multitenant.sample.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static final String[] EXCLUDED_PATHS = {
            // error endpoints
            "/error",

            // swagger resources
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    private final HandlerInterceptor multiTenantInterceptor;

    @Autowired
    public WebMvcConfig(HandlerInterceptor multiTenantInterceptor) {
        this.multiTenantInterceptor = multiTenantInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(multiTenantInterceptor)
                .excludePathPatterns(EXCLUDED_PATHS);
    }

}

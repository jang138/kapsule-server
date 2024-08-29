package net.kosa.kapsuleserver.base.config;

import lombok.RequiredArgsConstructor;
import net.kosa.kapsuleserver.base.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/v1/**")
                .addPathPatterns("/landmark/{id}")
                .excludePathPatterns("/api/v1/auth/**")
                .excludePathPatterns("/landmark") ;
    }
}

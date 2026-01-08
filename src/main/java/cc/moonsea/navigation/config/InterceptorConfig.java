package cc.moonsea.navigation.config;

import cc.moonsea.navigation.interceptor.VisitRecordInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final VisitRecordInterceptor visitRecordInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(visitRecordInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**",
                        "/js/**",
                        "/images/**",
                        "/icons/**",
                        "/favicon.ico");
    }
}

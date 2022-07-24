package me.cxis.starter.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignAutoConfiguration {

    @Bean
    public RequestTraceIdInterceptor requestTraceIdInterceptor() {
        return new RequestTraceIdInterceptor();
    }
}

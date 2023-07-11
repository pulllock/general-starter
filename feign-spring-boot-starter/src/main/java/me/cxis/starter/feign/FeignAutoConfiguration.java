package me.cxis.starter.feign;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FeignProperties.class)
public class FeignAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignProperties.class);

    @Bean
    @ConditionalOnProperty(prefix = "general.starter.feign", value = {"trace-enable"}, havingValue = "true")
    public RequestTraceIdInterceptor requestTraceIdInterceptor() {
        LOGGER.info("general starter feign request trace id interceptor enabled");
        return new RequestTraceIdInterceptor();
    }
}

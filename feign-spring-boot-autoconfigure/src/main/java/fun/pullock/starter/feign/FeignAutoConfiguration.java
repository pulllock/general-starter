package fun.pullock.starter.feign;

import fun.pullock.starter.feign.interceptor.RequestHeaderInterceptor;
import fun.pullock.starter.feign.interceptor.RequestTraceIdInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(prefix = "general.starter.feign", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfiguration
@EnableConfigurationProperties(FeignProperties.class)
public class FeignAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignProperties.class);

    @Bean
    @ConditionalOnProperty(prefix = "general.starter.feign", value = {"trace.enabled"}, havingValue = "true", matchIfMissing = true)
    public RequestTraceIdInterceptor requestTraceIdInterceptor() {
        LOGGER.info("General starter feign request trace id interceptor enabled");
        return new RequestTraceIdInterceptor();
    }

    @Bean
    @ConditionalOnProperty(prefix = "general.starter.feign", value = {"header.enabled"}, havingValue = "true", matchIfMissing = true)
    public RequestHeaderInterceptor requestHeaderInterceptor() {
        LOGGER.info("General starter feign request header interceptor enabled");
        return new RequestHeaderInterceptor();
    }
}

package fun.pullock.starter.feign;

import feign.Logger;
import feign.codec.Decoder;
import fun.pullock.starter.feign.decoder.FeignUnwrapDecoder;
import fun.pullock.starter.feign.interceptor.RequestHeaderInterceptor;
import fun.pullock.starter.feign.interceptor.RequestTraceIdInterceptor;
import fun.pullock.starter.feign.logger.FeignSlf4jInfoLogger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConditionalOnProperty(prefix = "general.starter.feign", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfiguration
@EnableConfigurationProperties(FeignProperties.class)
public class FeignAutoConfiguration {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeignProperties.class);

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

    @Bean
    @ConditionalOnProperty(prefix = "general.starter.feign", value = {"logger.enabled"}, havingValue = "true", matchIfMissing = true)
    public Logger logger() {
        LOGGER.info("General starter feign info logger enabled");
        return new FeignSlf4jInfoLogger();
    }

    @Bean
    @ConditionalOnProperty(prefix = "general.starter.feign", value = {"decoder.enabled"}, havingValue = "true", matchIfMissing = true)
    public Decoder decoder() {
        LOGGER.info("General starter feign decoder enabled");
        return new FeignUnwrapDecoder();
    }
}

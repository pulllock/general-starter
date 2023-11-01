package fun.pullock.starter.log;

import fun.pullock.starter.log.support.LogFilter;
import fun.pullock.starter.log.support.TraceIdFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LogProperties.class)
public class LogAutoConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogAutoConfiguration.class);

    /**
     * 注册TraceId过滤器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "general.starter.log", value = {"trace.enabled"}, havingValue = "true", matchIfMissing = true)
    public TraceIdFilter traceIdFilter() {
        LOGGER.info("General starter trace enabled");
        return new TraceIdFilter();
    }

    /**
     * 注册日志过滤器
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "general.starter.log", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
    public LogFilter logFilter() {
        LOGGER.info("General starter log enabled");
        return new LogFilter();
    }
}

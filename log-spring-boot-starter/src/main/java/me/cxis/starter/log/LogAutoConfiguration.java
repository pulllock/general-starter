package me.cxis.starter.log;

import me.cxis.starter.log.support.LogFilter;
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

    @Bean
    @ConditionalOnProperty(prefix = "general.starter.log", value = {"enable"}, havingValue = "true")
    public LogFilter logFilter() {
        LOGGER.info("general starter log enabled");
        return new LogFilter();
    }
}

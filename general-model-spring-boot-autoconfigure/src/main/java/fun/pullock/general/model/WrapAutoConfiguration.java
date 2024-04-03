package fun.pullock.general.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(WrapProperties.class)
public class WrapAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(WrapAutoConfiguration.class);

    /**
     * 注册全局异常处理器，如果已经存在了同名的全局异常处理器，则不进行注册。
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "general.starter.wrap", value = {"exception"}, havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
        LOGGER.info("General global exception handler enabled");
        return new GlobalExceptionHandler();
    }

    /**
     * 注册全局的结果包装处理器，如果已经存在了同名的全局结果包装处理器，则不进行注册。
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "general.starter.wrap", value = {"result"}, havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(WrapResultAdvice.class)
    public WrapResultAdvice wrapResultAdvice() {
        LOGGER.info("General wrap result advice enabled");
        return new WrapResultAdvice();
    }
}

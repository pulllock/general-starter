package me.cxis.general.model;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableConfigurationProperties(WrapProperties.class)
public class WrapAutoConfiguration {

    /**
     * 注册全局异常处理器，如果已经存在了同名的全局异常处理器，则不进行注册。
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "general.starter.wrap", value = {"exception"}, havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler() {
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
        return new WrapResultAdvice();
    }

    /**
     * 将MappingJackson2HttpMessageConverter放到最前面，可以解决方法返回String的时候统一包装报错的问题，
     * 另外需要注意，如果方法返回的是String，请在方法上添加produces = MediaType.APPLICATION_JSON_VALUE来进行配合使用。
     *
     * 如果返回的结果不需要进行统一包装(general.starter.wrap.result=false)，那么这里的配置也不会生效。
     * @param converter
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "general.starter.wrap", value = {"result"}, havingValue = "true", matchIfMissing = true)
    public WebMvcConfigurer mappingJackson2HttpMessageConverterConfigure(MappingJackson2HttpMessageConverter converter) {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(0, converter);
            }
        };
    }
}

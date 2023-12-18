package fun.pullock.starter.web;

import fun.pullock.starter.web.support.ClientHttpRequestLogInterceptor;
import fun.pullock.starter.web.support.ClientHttpRequestTraceIdInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@AutoConfiguration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebAutoConfiguration.class);

    private WebProperties webProperties;

    public WebAutoConfiguration(WebProperties webProperties) {
        this.webProperties = webProperties;
    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        LOGGER.info("General RestTemplate enabled");
        // 使用BufferingClientHttpRequestFactory，可以多次重复读取请求体和响应体
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();

        if (webProperties.getRestTemplate().isTraceEnabled()) {
            LOGGER.info("General RestTemplate trace id enabled");
            // 添加TraceId到请求头中的拦截器
            interceptors.add(new ClientHttpRequestTraceIdInterceptor());
        }

        if (webProperties.getRestTemplate().isLogEnabled()) {
            LOGGER.info("General RestTemplate log enabled");
            // RestTemplate请求的日志记录
            interceptors.add(new ClientHttpRequestLogInterceptor());
        }

        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }

}

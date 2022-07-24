package me.cxis.starter.web;

import me.cxis.starter.web.support.ClientHttpRequestLogInterceptor;
import me.cxis.starter.web.support.ClientHttpRequestTraceIdInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {

    @Value("${general.starter.web.logEnable:true}")
    private boolean restLogEnable;

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        // 使用BufferingClientHttpRequestFactory，可以多次重复读取请求体和响应体
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        // 添加TraceId到请求头中的拦截器
        interceptors.add(new ClientHttpRequestTraceIdInterceptor());

        if (restLogEnable) {
            // RestTemplate请求的日志记录
            interceptors.add(new ClientHttpRequestLogInterceptor());
        }

        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }
}

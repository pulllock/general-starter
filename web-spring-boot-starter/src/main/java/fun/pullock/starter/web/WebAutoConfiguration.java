package fun.pullock.starter.web;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import fun.pullock.starter.web.support.ClientHttpRequestLogInterceptor;
import fun.pullock.starter.web.support.ClientHttpRequestTraceIdInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@EnableConfigurationProperties(WebProperties.class)
public class WebAutoConfiguration {

    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

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

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // LocalDateTime格式
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT);

            // 序列化时LocalDateTime格式由默认的yyyy-MM-ddTHH:mm:ss修改为yyyy-MM-dd HH:mm:ss
            builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));

            // 反序列化时LocalDateTime格式由默认的yyyy-MM-ddTHH:mm:ss修改为yyyy-MM-dd HH:mm:ss
            builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
        };
    }
}

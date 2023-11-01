package fun.pullock.starter.jackson;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@ConditionalOnProperty(prefix = "general.starter.jackson", name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonAutoConfiguration {

    private final JacksonProperties jacksonProperties;

    public JacksonAutoConfiguration(JacksonProperties jacksonProperties) {
        this.jacksonProperties = jacksonProperties;
    }

    /**
     * 自定义Jackson序列化和反序列化格式
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 使用配置中自定义的LocalDateTime格式
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(jacksonProperties.getLocalDateTimePattern());

            // 序列化和反序列化时LocalDateTime格式使用自定义的格式，默认的格式：yyyy-MM-ddTHH:mm:ss
            builder.serializers(new LocalDateTimeSerializer(dateTimeFormatter));
            builder.deserializers(new LocalDateTimeDeserializer(dateTimeFormatter));
        };
    }
}

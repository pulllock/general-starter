package fun.pullock.starter.jackson;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.format.DateTimeFormatter;

@ConditionalOnProperty(prefix = "general.starter.jackson", name = "enabled", havingValue = "true", matchIfMissing = true)
@AutoConfiguration
@EnableConfigurationProperties(JacksonProperties.class)
public class JacksonAutoConfiguration {

    private final JacksonProperties jacksonProperties;

    public JacksonAutoConfiguration(JacksonProperties jacksonProperties) {
        this.jacksonProperties = jacksonProperties;
    }

    /**
     * 自定义Jackson的ObjectMapper
     * @return
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 使用配置中自定义的LocalDateTime格式
            DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern(jacksonProperties.getLocalDateTimePattern());

            // 序列化和反序列化时LocalDateTime格式使用自定义的格式，默认的格式：yyyy-MM-ddTHH:mm:ss
            builder.serializers(new LocalDateTimeSerializer(localDateTimeFormatter));
            builder.deserializers(new LocalDateTimeDeserializer(localDateTimeFormatter));

            // 使用配置中自定义的LocalDate格式
            DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern(jacksonProperties.getLocalDatePattern());

            // 序列化和反序列化时LocalDate格式使用自定义的格式，默认的格式：yyyy-MM-dd
            builder.serializers(new LocalDateSerializer(localDateFormatter));
            builder.deserializers(new LocalDateDeserializer(localDateFormatter));

            // 使用配置中自定义的LocalTime格式
            DateTimeFormatter localTimeFormatter = DateTimeFormatter.ofPattern(jacksonProperties.getLocalTimePattern());

            // 序列化和反序列化时LocalTime格式使用自定义的格式，默认的格式：yyyy-MM-dd
            builder.serializers(new LocalTimeSerializer(localTimeFormatter));
            builder.deserializers(new LocalTimeDeserializer(localTimeFormatter));
        };
    }
}

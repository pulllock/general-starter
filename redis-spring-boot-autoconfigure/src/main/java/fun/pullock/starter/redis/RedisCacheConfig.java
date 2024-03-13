package fun.pullock.starter.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import fun.pullock.gneral.constant.JacksonConfigDefinition;
import fun.pullock.starter.redis.lock.RedisLock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;
import static fun.pullock.gneral.constant.JacksonConfigDefinition.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair.fromSerializer;

@AutoConfiguration
@EnableCaching
public class RedisCacheConfig {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * redis缓存通用配置
     * @return
     */
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                // 缓存命名空间前缀
                .prefixCacheNameWith(String.format("%s::", appName))
                // 全局默认缓存时间：5分钟
                .entryTtl(Duration.ofMinutes(5))
                // key序列化方式：String
                .serializeKeysWith(fromSerializer(new StringRedisSerializer(UTF_8)))
                // 使用自定义的value序列化方式
                .serializeValuesWith(fromSerializer(genericJackson2JsonRedisSerializer()))
                // 不缓存null值
                .disableCachingNullValues()
                ;
    }

    /**
     * 自定义RedisTemplate<String, Object>配置
     *
     * <p>StringRedisTemplate序列化方式为字符串，无需做自定义配置，而RedisTemplate<Object, Object>默认的序列化方式为JDK序列化，一般会修改为自定义的JSON序列化方式</p>
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(name = "stringObjectRedisTemplate")
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> stringObjectRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key序列化方式：String
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        // value使用自定义序列化方式
        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer());
        // Hash的key序列化方式：String
        redisTemplate.setHashKeySerializer(StringRedisSerializer.UTF_8);
        // Hash的value使用自定义序列化方式
        redisTemplate.setHashValueSerializer(genericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * Redis锁
     * @return
     */
    @Bean
    public RedisLock redisLock(RedisTemplate<String, Object> stringObjectRedisTemplate) {
        return new RedisLock(stringObjectRedisTemplate, appName);
    }

    /**
     * 自定义GenericJackson2JsonRedisSerializer
     * @return
     */
    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册自定义的时间序列化和反序列化格式
        objectMapper.registerModule(customJavaTimeModule());

        // 在属性中添加@class信息，用于反序列化时所需的类型信息，反序列化时如果没有类型信息会出错
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), NON_FINAL, As.PROPERTY);

        // 配置反序列化时允许未知属性
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                JacksonConfigDefinition.FAIL_ON_UNKNOWN_PROPERTIES
        );

        // 配置序列化时允许空Bean
        objectMapper.configure(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                JacksonConfigDefinition.FAIL_ON_EMPTY_BEANS
        );

        // 配置null值序列化器
        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    /**
     * 自定义Java时间序列化和反序列化格式
     * @return
     */
    private JavaTimeModule customJavaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // LocalDateTime序列化格式
        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(ofPattern(LOCAL_DATE_TIME_PATTERN))
        );

        // LocalDate序列化格式
        javaTimeModule.addSerializer(
                LocalDate.class,
                new LocalDateSerializer(ofPattern(LOCAL_DATE_PATTERN))
        );

        // LocalTime序列化格式
        javaTimeModule.addSerializer(
                LocalTime.class,
                new LocalTimeSerializer(ofPattern(LOCAL_TIME_PATTERN))
        );

        // LocalDateTime反序列化格式
        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(ofPattern(LOCAL_DATE_TIME_PATTERN))
        );

        // LocalDate反序列化格式
        javaTimeModule.addDeserializer(
                LocalDate.class,
                new LocalDateDeserializer(ofPattern(LOCAL_DATE_PATTERN))
        );

        // LocalTime反序列化格式
        javaTimeModule.addDeserializer(
                LocalTime.class,
                new LocalTimeDeserializer(ofPattern(LOCAL_TIME_PATTERN))
        );
        return javaTimeModule;
    }
}

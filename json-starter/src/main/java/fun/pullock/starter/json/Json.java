package fun.pullock.starter.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import fun.pullock.gneral.constant.JacksonConfigDefinition;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static fun.pullock.gneral.constant.JacksonConfigDefinition.LOCAL_DATE_PATTERN;
import static fun.pullock.gneral.constant.JacksonConfigDefinition.LOCAL_DATE_TIME_PATTERN;

public class Json {

    private static final String LOCAL_DATE_FORMAT = LOCAL_DATE_PATTERN;

    private static final String LOCAL_DATE_TIME_FORMAT = LOCAL_DATE_TIME_PATTERN;

    private static final ObjectMapper OBJECT_MAPPER;

    private Json() {}

    static {
        OBJECT_MAPPER = new ObjectMapper();

        // 配置反序列化时允许未知属性
        OBJECT_MAPPER.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                JacksonConfigDefinition.FAIL_ON_UNKNOWN_PROPERTIES
        );

        // 配置反序列化时允许基本类型为null
        OBJECT_MAPPER.configure(
                DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                JacksonConfigDefinition.FAIL_ON_NULL_FOR_PRIMITIVES
        );

        // 配置允许使用单引号
        OBJECT_MAPPER.configure(
                JsonParser.Feature.ALLOW_SINGLE_QUOTES,
                JacksonConfigDefinition.ALLOW_SINGLE_QUOTES
        );

        // 配置序列化时允许空Bean
        OBJECT_MAPPER.configure(
                SerializationFeature.FAIL_ON_EMPTY_BEANS,
                JacksonConfigDefinition.FAIL_ON_EMPTY_BEANS
        );

        // 配置序列化时只包含不为空的字段
        OBJECT_MAPPER.setSerializationInclusion(
                JsonInclude.Include.valueOf(JacksonConfigDefinition.JSON_INCLUDE_NON_NULL)
        );

        // 配置反序列化时开启将浮点数解析成BigDecimal对象，不开启的时候默认会解析成Double对象
        OBJECT_MAPPER.configure(
                DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS,
                JacksonConfigDefinition.USE_BIG_DECIMAL_FOR_FLOATS
        );

        // Java日期格式序列化和反序列化配置
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 更改LocalDate类型字段序列化的格式为yyyy-MM-dd
        javaTimeModule.addSerializer(
                LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)));

        // 更改LocalDate类型字段反序列化的格式为yyyy-MM-dd
        javaTimeModule.addDeserializer(
                LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)));

        // 更改LocalDateTime类型字段序列化的格式为yyyy-MM-dd HH:mm:ss
        javaTimeModule.addSerializer(
                LocalDateTime.class,
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));

        // 更改LocalDateTime类型字段反序列化的格式为yyyy-MM-dd HH:mm:ss
        javaTimeModule.addDeserializer(
                LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_FORMAT)));

        OBJECT_MAPPER.registerModule(javaTimeModule);
    }

    public static String toJson(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObjectNode toObject(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return (ObjectNode) OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toArray(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        try {
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean validate(String json) {
        if (json == null || json.isEmpty()) {
            return false;
        }

        try {
            OBJECT_MAPPER.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public static boolean isArray(String json) {
        if (json == null || json.isEmpty()) {
            return false;
        }

        try {
            JsonNode jsonNode = OBJECT_MAPPER.readTree(json);
            return jsonNode.isArray();
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public static ObjectNode createObjectNode() {
        return OBJECT_MAPPER.createObjectNode();
    }
}

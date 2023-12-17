package fun.pullock.gneral.constant;

import static fun.pullock.gneral.constant.DateTimeFormatDefinition.*;

/**
 * Jackson配置定义
 */
public class JacksonConfigDefinition {

    /**
     * 默认LocalDateTime格式
     */
    public static final String LOCAL_DATE_TIME_PATTERN = DEFAULT_LOCAL_DATE_TIME_PATTERN;

    /**
     * 默认LocalDate格式
     */
    public static final String LOCAL_DATE_PATTERN = DEFAULT_LOCAL_DATE_PATTERN;

    /**
     * 默认LocalTime格式
     */
    public static final String LOCAL_TIME_PATTERN = DEFAULT_LOCAL_TIME_PATTERN;

    /**
     * 反序列化时允许未知属性
     */
    public static final boolean FAIL_ON_UNKNOWN_PROPERTIES = false;

    /**
     * 反序列化时允许基本类型为null
     */
    public static final boolean FAIL_ON_NULL_FOR_PRIMITIVES = false;

    /**
     * 允许使用单引号
     */
    public static final boolean ALLOW_SINGLE_QUOTES = true;

    /**
     * 序列化时允许空Bean
     */
    public static final boolean FAIL_ON_EMPTY_BEANS = false;

    /**
     * 序列化时只包含不为空的字段
     */
    public static final String JSON_INCLUDE_NON_NULL = "NON_NULL";

    /**
     * 反序列化时开启将浮点数解析成BigDecimal对象，不开启的时候默认会解析成Double对象
     */
    public static final boolean USE_BIG_DECIMAL_FOR_FLOATS = true;
}

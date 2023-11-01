package fun.pullock.starter.jackson;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "general.starter.jackson")
public class JacksonProperties {

    /**
     * 默认LocalDateTime格式
     */
    private static final String DEFAULT_LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认LocalDate格式
     */
    private static final String DEFAULT_LOCAL_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * LocalDateTime格式
     */
    private String localDateTimePattern = DEFAULT_LOCAL_DATE_TIME_PATTERN;

    /**
     * LocalDate格式
     */
    private String localDatePattern = DEFAULT_LOCAL_DATE_PATTERN;

    public String getLocalDateTimePattern() {
        return localDateTimePattern;
    }

    public void setLocalDateTimePattern(String localDateTimePattern) {
        this.localDateTimePattern = localDateTimePattern;
    }

    public String getLocalDatePattern() {
        return localDatePattern;
    }

    public void setLocalDatePattern(String localDatePattern) {
        this.localDatePattern = localDatePattern;
    }
}

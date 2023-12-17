package fun.pullock.starter.jackson;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static fun.pullock.gneral.constant.JacksonConfigDefinition.*;

@ConfigurationProperties(prefix = "general.starter.jackson")
public class JacksonProperties {

    /**
     * LocalDateTime格式
     */
    private String localDateTimePattern = LOCAL_DATE_TIME_PATTERN;

    /**
     * LocalDate格式
     */
    private String localDatePattern = LOCAL_DATE_PATTERN;

    /**
     * LocalTime格式
     */
    private String localTimePattern = LOCAL_TIME_PATTERN;

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

    public String getLocalTimePattern() {
        return localTimePattern;
    }

    public void setLocalTimePattern(String localTimePattern) {
        this.localTimePattern = localTimePattern;
    }
}

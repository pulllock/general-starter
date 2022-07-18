package me.cxis.general.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "general.starter.wrap")
public class WrapProperties {

    /**
     * 是否启用结果包装，默认启用
     */
    private boolean result = true;

    /**
     * 是否启用结果包装，默认启用
     */
    private boolean exception = true;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isException() {
        return exception;
    }

    public void setException(boolean exception) {
        this.exception = exception;
    }
}

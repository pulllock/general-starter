package fun.pullock.starter.log;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "general.starter.log")
public class LogProperties {

    /**
     * 是否启用日志
     */
    private boolean enable = false;

    /**
     * 是否启用traceId
     */
    private boolean traceEnable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isTraceEnable() {
        return traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
}

package me.cxis.starter.log;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "general.starter.log")
public class LogProperties {

    /**
     * 是否启用日志
     */
    private boolean enable = false;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}

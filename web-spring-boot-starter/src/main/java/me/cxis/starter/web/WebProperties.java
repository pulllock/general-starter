package me.cxis.starter.web;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "general.starter.web")
public class WebProperties {

    /**
     * 是否启用日志
     */
    private boolean logEnable = true;

    public boolean isLogEnable() {
        return logEnable;
    }

    public void setLogEnable(boolean logEnable) {
        this.logEnable = logEnable;
    }
}

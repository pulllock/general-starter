package fun.pullock.starter.feign;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "general.starter.feign")
public class FeignProperties {

    /**
     * 是否启用traceId
     */
    private boolean traceEnable = false;

    public boolean isTraceEnable() {
        return traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }
}

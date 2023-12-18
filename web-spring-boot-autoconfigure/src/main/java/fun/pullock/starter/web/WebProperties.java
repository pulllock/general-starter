package fun.pullock.starter.web;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "general.starter.web")
public class WebProperties {

    private RestTemplate restTemplate = new RestTemplate();

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class RestTemplate {

        /**
         * 是否启用RestTemplate日志
         */
        private boolean logEnabled = true;

        /**
         * 使用启用RestTemplate的trace id功能
         */
        private boolean traceEnabled = true;

        public boolean isLogEnabled() {
            return logEnabled;
        }

        public void setLogEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
        }

        public boolean isTraceEnabled() {
            return traceEnabled;
        }

        public void setTraceEnabled(boolean traceEnabled) {
            this.traceEnabled = traceEnabled;
        }
    }
}

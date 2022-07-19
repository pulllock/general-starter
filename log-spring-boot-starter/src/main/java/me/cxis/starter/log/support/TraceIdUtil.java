package me.cxis.starter.log.support;

import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * TraceId工具类
 */
public class TraceIdUtil {

    /**
     * 放入到MDC中的TraceId的Key
     */
    public final static String TRACE_ID_KEY = "traceId";

    /**
     * 请求头中的请求ID对应的Key
     */
    public final static String X_REQUEST_ID = "x-request-id";

    /**
     * 设置traceId到MDC中
     * @param traceId
     */
    public static void setTraceId(String traceId) {
        if (!StringUtils.hasLength(traceId)) {
            traceId = generateTraceId();
        }

        MDC.put(TRACE_ID_KEY, traceId);
    }

    /**
     * 从MDC中获取traceId
     * @return
     */
    public static String getTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * 使用UUID生成traceId
     * @return
     */
    private static String generateTraceId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

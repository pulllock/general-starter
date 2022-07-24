package me.cxis.starter.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import me.cxis.starter.log.support.TraceIdUtil;
import org.springframework.core.annotation.Order;

/**
 * Feign调用添加TraceId
 */
@Order(0)
public class RequestTraceIdInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TraceIdUtil.X_REQUEST_ID, TraceIdUtil.getTraceId());
    }
}

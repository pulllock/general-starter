package fun.pullock.starter.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import fun.pullock.starter.log.support.TraceIdUtil;

/**
 * Feign调用添加TraceId
 */
public class RequestTraceIdInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(TraceIdUtil.X_REQUEST_ID, TraceIdUtil.getTraceId());
    }
}

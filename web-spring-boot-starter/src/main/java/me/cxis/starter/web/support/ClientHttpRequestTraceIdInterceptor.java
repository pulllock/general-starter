package me.cxis.starter.web.support;

import me.cxis.starter.log.support.TraceIdUtil;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * 拦截RequestTemplate的请求，在请求头中添加traceId
 */
@Order(0)
public class ClientHttpRequestTraceIdInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add(TraceIdUtil.X_REQUEST_ID, TraceIdUtil.getTraceId());
        return execution.execute(request, body);
    }
}

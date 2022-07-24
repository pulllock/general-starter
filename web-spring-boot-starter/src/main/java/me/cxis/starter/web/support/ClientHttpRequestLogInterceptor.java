package me.cxis.starter.web.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 拦截RequestTemplate的请求，打印日志
 */
@Order(0)
public class ClientHttpRequestLogInterceptor implements ClientHttpRequestInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientHttpRequestLogInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // 打印请求日志
        logForRequest(request, body);

        ClientHttpResponse response = execution.execute(request, body);
        // 打印响应日志
        logForResponse(request, response);
        return response;
    }

    private void logForResponse(HttpRequest request, ClientHttpResponse response) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("Response ");
        msg.append(request.getMethod()).append(' ');
        msg.append(request.getURI());

        /*String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }*/

        String ip = getIp(request);
        if (StringUtils.hasLength(ip)) {
            msg.append(", client=").append(ip);
        }

        String referer = request.getHeaders().getFirst("referer");
        if (StringUtils.hasLength(referer)) {
            msg.append(", referer=").append(referer);
        }
        msg.append(", status=").append(response.getRawStatusCode());

        String payload = getResponseMessagePayload(response);
        if (payload != null) {
            msg.append(", payload=").append(payload);
        }

        LOGGER.info(msg.toString());
    }

    private void logForRequest(HttpRequest request, byte[] body) {
        StringBuilder msg = new StringBuilder();
        msg.append("Request ");
        msg.append(request.getMethod()).append(' ');
        msg.append(request.getURI());

        /*String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }*/

        String ip = getIp(request);
        if (StringUtils.hasLength(ip)) {
            msg.append(", client=").append(ip);
        }

        String referer = request.getHeaders().getFirst("referer");
        if (StringUtils.hasLength(referer)) {
            msg.append(", referer=").append(referer);
        }

        String payload = getRequestMessagePayload(body);
        if (payload != null) {
            msg.append(", payload=").append(payload);
        }

        LOGGER.info(msg.toString());
    }

    private String getIp(HttpRequest request) {
        String xff = request.getHeaders().getFirst("x-forwarded-for");
        if (xff != null && xff.length() > 0) {
            return xff.split(",")[0];
        }

        // return request.getRemoteAddr();
        return null;
    }

    protected String getRequestMessagePayload(byte[] body) {
        if (body.length > 0) {
            int length = body.length;
            return new String(body, 0, length, StandardCharsets.UTF_8);
        }

        return null;
    }

    private String getResponseMessagePayload(ClientHttpResponse response) throws IOException {

        byte[] buf = StreamUtils.copyToByteArray(response.getBody());
        if (buf.length > 0) {
            int length = buf.length;
            return new String(buf, 0, length, StandardCharsets.UTF_8);
        }
        return null;
    }
}

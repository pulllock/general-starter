package me.cxis.starter.log.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

/**
 * 日志打印过滤器
 */
public class LogFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);

    /**
     * 处理逻辑可参考：{@link AbstractRequestLoggingFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)}
     * 以及{@link ShallowEtagHeaderFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)}
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        HttpServletRequest requestToUse = request;

        if (isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request);
        }

        HttpServletResponse responseToUse = response;
        if (isFirstRequest && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        if (isFirstRequest) {
            // 打印请求日志
            logForRequest(requestToUse);
        }

        try {
            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            if (isFirstRequest) {
                ContentCachingResponseWrapper wrappedResponse = (ContentCachingResponseWrapper) responseToUse;
                // 打印响应日志
                logForResponse(requestToUse, wrappedResponse);
                // 记得拷贝回去
                wrappedResponse.copyBodyToResponse();
            }
        }

    }

    private void logForResponse(HttpServletRequest request, ContentCachingResponseWrapper response) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("Response ");
        msg.append(request.getMethod()).append(' ');
        msg.append(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }

        String ip = getIp(request);
        if (StringUtils.hasLength(ip)) {
            msg.append(", client=").append(ip);
        }

        String referer = request.getHeader("referer");
        if (StringUtils.hasLength(referer)) {
            msg.append(", referer=").append(referer);
        }
        msg.append(", status=").append(response.getStatus());

        String payload = getResponseMessagePayload(response);
        if (payload != null) {
            msg.append(", payload=").append(payload);
        }

        LOGGER.info(msg.toString());
    }

    private void logForRequest(HttpServletRequest request) throws IOException {
        StringBuilder msg = new StringBuilder();
        msg.append("Request ");
        msg.append(request.getMethod()).append(' ');
        msg.append(request.getRequestURI());

        String queryString = request.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }

        String ip = getIp(request);
        if (StringUtils.hasLength(ip)) {
            msg.append(", client=").append(ip);
        }

        String referer = request.getHeader("referer");
        if (StringUtils.hasLength(referer)) {
            msg.append(", referer=").append(referer);
        }

        String payload;
        String contentType = request.getContentType();
        if (contentType != null && contentType.equals(MULTIPART_FORM_DATA_VALUE)) {
            payload = "MULTIPART_FORM_DATA";
        } else {
            payload = getRequestMessagePayload(request);
            if (payload != null) {
                msg.append(", payload=").append(payload);
            }
        }

        LOGGER.info(msg.toString());
    }

    private String getIp(HttpServletRequest request) {
        String xff = request.getHeader("x-forwarded-for");
        if (xff != null && xff.length() > 0) {
            return xff.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    protected String getRequestMessagePayload(HttpServletRequest request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getCachedContent();
            if (buf.length > 0) {
                int length = buf.length;
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                }
                catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return null;
    }

    private String getResponseMessagePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = buf.length;
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                }
                catch (UnsupportedEncodingException ex) {
                    return "[unknown]";
                }
            }
        }
        return null;
    }
}

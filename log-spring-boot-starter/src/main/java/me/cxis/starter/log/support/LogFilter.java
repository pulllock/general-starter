package me.cxis.starter.log.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

public class LogFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(LogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        logForRequest(wrappedRequest);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        logForResponse(wrappedRequest, wrappedResponse);
        wrappedResponse.copyBodyToResponse();
    }

    private void logForResponse(ContentCachingRequestWrapper wrappedRequest, ContentCachingResponseWrapper wrappedResponse) throws IOException {
        String referer = wrappedRequest.getHeader("referer");
        String method = wrappedRequest.getMethod();
        String uri = wrappedRequest.getRequestURI();
        String parameters = getRequestParameters(wrappedRequest);
        String ip = getIp(wrappedRequest);
        String contentType = wrappedRequest.getContentType();

        String responseBody = StreamUtils.copyToString(wrappedResponse.getContentInputStream(), Charset.forName(wrappedResponse.getCharacterEncoding()));
        LOGGER.info(
                "Response, refer: {}, ip: {}, content type: {}, method: {}, URI: {}, parameters: {}, response body: {}",
                referer, ip, contentType, method, uri, parameters, responseBody
        );
    }

    private void logForRequest(ContentCachingRequestWrapper wrappedRequest) throws IOException {
        String referer = wrappedRequest.getHeader("referer");
        String method = wrappedRequest.getMethod();
        String uri = wrappedRequest.getRequestURI();
        String parameters = getRequestParameters(wrappedRequest);
        String ip = getIp(wrappedRequest);
        String contentType = wrappedRequest.getContentType();

        String requestBody;
        if (contentType != null && contentType.equals(MULTIPART_FORM_DATA_VALUE)) {
            requestBody = "MULTIPART_FORM_DATA";
        } else {
            requestBody = StreamUtils.copyToString(wrappedRequest.getInputStream(), Charset.forName(wrappedRequest.getCharacterEncoding()));
        }

        LOGGER.info(
                "Request, refer: {}, ip: {}, content type: {}, method: {}, URI: {}, parameters: {}, request body: {}",
                referer, ip, contentType, method, uri, parameters, requestBody
        );
    }

    private String getIp(HttpServletRequest request) {
        String xff = request.getHeader("x-forwarded-for");
        if (xff != null && xff.length() > 0) {
            return xff.split(",")[0];
        }

        return request.getRemoteAddr();
    }

    private String getRequestParameters(HttpServletRequest request) {
        StringBuilder result = new StringBuilder();
        Enumeration<String> names = request.getParameterNames();
        if (names == null) {
            return null;
        }

        result.append("?");

        while (names.hasMoreElements()) {
            if (result.length() > 1) {
                result.append("&");
            }

            String current = names.nextElement();
            result.append(current).append("=");
            result.append(request.getParameter(current));
        }
        return result.toString();
    }
}

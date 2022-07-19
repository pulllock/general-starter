package me.cxis.starter.log.support;

import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static me.cxis.starter.log.support.TraceIdUtil.X_REQUEST_ID;

/**
 * 设置TraceId的过滤器，实现原理和{@link LogFilter}基本一致
 *
 * 需要设置TraceIdFilter的优先级高一点。
 */
@Order(0)
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        boolean isFirstRequest = !isAsyncDispatch(request);
        if (isFirstRequest) {
            String requestId = request.getHeader(X_REQUEST_ID);
            // 请求头中有请求ID，直接设置到MDC中
            if (StringUtils.hasLength(requestId)) {
                TraceIdUtil.setTraceId(requestId);
            } else {
                // 请求头中没有请求ID，生成一个UUID作为请求ID
                TraceIdUtil.setTraceId(null);
            }
        }

        filterChain.doFilter(request, response);
    }
}

package me.cxis.general.model;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 对返回结果使用{@link Result}进行包装，如果使用了{@link RawResult}注解，则不进行包装。
 */
@ControllerAdvice
public class WrapResultAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 类上面有@RawResult注解，则不需要对结果进行包装
        if (returnType.getDeclaringClass().isAnnotationPresent(RawResult.class)) {
            return false;
        }

        // 方法上面有@RawResult注解，则不需要对结果进行包装
        return returnType.getMethodAnnotation(RawResult.class) == null;
    }

    /**
     * 对没有使用{@link Result}进行包装的返回值进行包装
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request the current request
     * @param response the current response
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return new Result<>();
        }

        if (!(body instanceof Result)) {
            return new Result<>(body);
        }

        return body;
    }
}

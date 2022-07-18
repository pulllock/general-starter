package me.cxis.general.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static me.cxis.general.model.CommonErrorCode.SYSTEM_ERROR;

/**
 * 全局异常统一处理，统一将异常包装成{@link Result}返回
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理ServiceException类型的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = ServiceException.class)
    public Result<Nil> handleServiceException(ServiceException e) {
        LOGGER.error("ServiceException: ", e);
        return new Result<>(e.getErrorCode(), e.getErrorMsg(), null);
    }

    /**
     * 处理Exception类型的异常
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Result<Nil> handleException(Exception e) {
        LOGGER.error("Exception: ", e);
        return new Result<>(SYSTEM_ERROR.getErrorCode(), SYSTEM_ERROR.getErrorMsg(), null);
    }
}

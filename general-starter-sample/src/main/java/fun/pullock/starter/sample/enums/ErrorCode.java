package fun.pullock.starter.sample.enums;

import fun.pullock.general.model.BaseErrorCode;

/**
 * 业务错误码
 */
public enum ErrorCode implements BaseErrorCode {

    USER_NOT_EXIST (10001, "用户不存在")
    ;

    ErrorCode(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private int errorCode;

    private String errorMsg;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}

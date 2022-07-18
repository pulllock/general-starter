package me.cxis.starter.sample.enums;

import me.cxis.general.model.BaseErrorCode;

public enum ErrorCode implements BaseErrorCode {

    SYSTEM_ERROR (1, "系统错误")
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

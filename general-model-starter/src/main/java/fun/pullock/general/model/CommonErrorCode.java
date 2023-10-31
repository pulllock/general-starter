package fun.pullock.general.model;

/**
 * 所有系统共用的通用错误码
 */
public enum CommonErrorCode implements BaseErrorCode {

    UNAUTHORIZED (401, "未授权的请求"),
    SYSTEM_ERROR (500, "系统错误")
    ;

    CommonErrorCode(int errorCode, String errorMsg) {
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

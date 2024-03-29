package fun.pullock.general.model;

/**
 * 业务服务异常
 */
public class ServiceException extends RuntimeException {

    private int errorCode;

    private String errorMsg;

    public ServiceException(BaseErrorCode errorCode) {
        super(String.format("%s %s", errorCode.getErrorCode(), errorCode.getErrorMsg()));
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = errorCode.getErrorMsg();
    }

    public ServiceException(int errorCode, String errorMsg) {
        super(String.format("%s %s", errorCode, errorMsg));
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ServiceException(BaseErrorCode errorCode, String errorMsg) {
        super(String.format("%s %s %s", errorCode.getErrorCode(), errorCode.getErrorMsg(), errorMsg));
        this.errorCode = errorCode.getErrorCode();
        this.errorMsg = String.format("%s: %s", errorCode.getErrorMsg(), errorMsg);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

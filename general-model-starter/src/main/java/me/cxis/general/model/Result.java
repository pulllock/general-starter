package me.cxis.general.model;

import java.io.Serializable;

/**
 * 结果
 * @param <T>
 */
public class Result<T> implements Serializable {

    /**
     * 如果是错误返回，则是具体的错误码；如果是正确的返回，则code=0
     */
    private int code = 0;

    /**
     * 返回的消息，如果是错误需要返回错误信息；如果是正确的返回，也可以添加信息
     */
    private String msg;

    /**
     * 是否成功
     */
    private boolean success = true;

    /**
     * 返回的实际数据，如果是错误的，则为null
     */
    private T data;

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", success=" + success +
                ", data=" + data +
                '}';
    }
}

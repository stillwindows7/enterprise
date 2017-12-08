package io.github.enterprise.utils.common;

/**
 * Created by Sheldon on 2017/12/08
 */
public class BusinessException extends RuntimeException {
    private String code = "-1";

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

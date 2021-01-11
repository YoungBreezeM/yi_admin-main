package com.fw.core.domain;



/**
 * @author yqf
 */

public enum ResultType {

    /**基础类型*/
    SUCCESS(0,"success"),
    FAIL(201,"fail"),

    /**用户操作*/
    LOGIN_SUCCESS(0,"login success"),
    LOGIN_FAIL(201,"username or password id error"),

    /**文件操作*/
    UPLOAD_SUCCESS(0,"upload success"),
    UPLOAD_FAIL(200,"upload success"),
    /**安全验证*/
    TOKEN_VERIFY_FAIL(500,"token verify fail");

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResultType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

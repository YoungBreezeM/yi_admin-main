package com.fw.wx.domain;


/**
 * @author yqf
 */

public enum WxResType {

    /**
     * 微信接口
     */
    LOGIN_SUCCESS(0, "login success"),
    LOGIN_FAIL(500, "login fail"),
    /**安全验证*/
    TOKEN_VERIFY_FAIL(500,"token verify fail"),
    FAIL(300,"fail"),
    SUCCESS(0,"success");

    private Integer code;

    private String msg;

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    WxResType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

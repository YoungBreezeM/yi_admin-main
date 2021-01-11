package com.fw.wx.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yqf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxRes implements Serializable {

    private Integer code;

    private String msg;

    private Object data;

    public WxRes(WxResType wxResType, String msg, Object data) {

        this.code = wxResType.getCode();
        this.msg = msg;
        this.data = data;
    }

    public WxRes(WxResType wxResType) {
        this.code = wxResType.getCode();
        this.msg = wxResType.getMsg();
    }

    public WxRes(WxResType wxResType, Object data) {
        this.code = wxResType.getCode();
        this.msg = wxResType.getMsg();
        this.data = data;
    }
}

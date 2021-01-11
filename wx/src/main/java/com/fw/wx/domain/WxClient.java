package com.fw.wx.domain;

import lombok.Data;

/**
 * @author yqf
 * @date 2020/10/27 下午2:39
 */
@Data
public class WxClient {
    private String sessionKey;
    private String openid;
}

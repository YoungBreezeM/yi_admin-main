package com.fw.wx.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author yqf
 * Date: 2020/10/11
 * Time: 下午5:24
 * Description: No Description
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.mini")
public class WxConfig {
    private String appId;
    private String secret;
    private String token;
    private String aesKey;
}

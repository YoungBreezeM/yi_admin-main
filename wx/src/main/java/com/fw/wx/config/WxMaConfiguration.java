package com.fw.wx.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaInMemoryConfig;

import com.fw.core.utils.TokenUtil;
import com.fw.wx.domain.WxConfig;
import jdk.nashorn.internal.parser.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yqf
 */
@Configuration
public class WxMaConfiguration {

    @Autowired
    private WxConfig wxConfig;

    @Bean
    public WxMaService wxMaService() {

        WxMaInMemoryConfig config = new WxMaInMemoryConfig();
        config.setAppid(wxConfig.getAppId());
        config.setSecret(wxConfig.getSecret());
        config.setToken(wxConfig.getToken());
        config.setAesKey(wxConfig.getAesKey());

        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(config);

        return wxMaService;
    }

    @Bean
    public TokenUtil tokenUtil(){
        TokenUtil tokenUtil = new TokenUtil();
        tokenUtil.setTokenSecret("wx-secret");
        return tokenUtil;
    }

}
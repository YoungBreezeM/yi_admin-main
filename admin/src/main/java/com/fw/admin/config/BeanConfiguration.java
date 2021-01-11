package com.fw.admin.config;

import com.fw.core.utils.TokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yqf
 * @date 2020/10/28 上午10:15
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public TokenUtil tokenUtil(){
        TokenUtil tokenUtil = new TokenUtil();
        tokenUtil.setTokenSecret("admin-secret");

        return tokenUtil;
    }
}

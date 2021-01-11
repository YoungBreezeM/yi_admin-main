package com.fw.wx.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author yqf
 * @date 2020 上午10:23
 */
@Component
@ConfigurationProperties(prefix = "file")
@Data
public class FileProperties {

    private String uploadDir;

}

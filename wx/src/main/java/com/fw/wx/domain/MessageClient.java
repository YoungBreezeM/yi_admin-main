package com.fw.wx.domain;

import com.fw.core.entity.Answer;
import com.fw.core.entity.Client;
import com.fw.core.entity.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yqf
 * @date 2020/11/30 下午4:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageClient implements Serializable {
    private Boolean status;
    private Client client;
    private Answer answer;
    private Questions questions;
    private String url;
}

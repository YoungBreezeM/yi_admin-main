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
 * @date 2020/11/21 下午5:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerClient implements Serializable {
    private Answer answer;
    private Client client;
    private Questions questions;
}

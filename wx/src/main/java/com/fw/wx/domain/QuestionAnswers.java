package com.fw.wx.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqf
 * @date 2020/11/21 下午5:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAnswers implements Serializable {
    private QuestionClient questionClient;
    private List<AnswerClient> answerClients;
}

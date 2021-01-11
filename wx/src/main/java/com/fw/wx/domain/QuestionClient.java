package com.fw.wx.domain;

import com.fw.core.entity.Client;
import com.fw.core.entity.Questions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqf
 * @date 2020/11/18 下午8:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionClient implements Serializable {
    private Client client;
    private Questions questions;
    private List<String> imgList;
}

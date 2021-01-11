package com.fw.wx.domain;

import com.fw.core.entity.Client;
import com.fw.core.entity.Prediction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yqf
 * @date 2021/1/4 下午2:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientPrediction implements Serializable {
    private Client client;
    private Prediction prediction;

}

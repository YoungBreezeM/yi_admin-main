package com.fw.wx.domain;

import com.fw.core.entity.BaseGua;
import com.fw.core.entity.Yao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author yqf
 * @date 2020/11/11 下午7:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComputeGua implements Serializable {
    private List<Integer> top;
    private List<Integer> bottom;
    private List<Boolean> changeGua;
    private Integer pos;
    private Boolean change;
}

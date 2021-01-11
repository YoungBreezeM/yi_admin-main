package com.fw.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yqf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result implements Serializable {

    private Integer code;

    private String msg;

    private Object data;

    public Result(ResultType resultType, String msg, Object data) {

        this.code = resultType.getCode();
        this.msg = msg;
        this.data = data;
    }

    public Result(ResultType resultType) {
        this.code = resultType.getCode();
        this.msg = resultType.getMsg();
    }

    public Result(ResultType resultType, Object data){
        this.code = resultType.getCode();
        this.msg = resultType.getMsg();
        this.data = data;
    }
}

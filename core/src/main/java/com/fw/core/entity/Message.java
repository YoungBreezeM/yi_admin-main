package com.fw.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer answerId;

    private Integer questionId;

    private Boolean status;

    private Date time;



}

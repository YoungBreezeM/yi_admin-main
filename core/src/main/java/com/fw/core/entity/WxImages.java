package com.fw.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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
public class WxImages implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
       
    private Integer questionId;

    @TableField("uploadTime")
    private Date uploadTime;

    @TableField("url")
    private String url;


}

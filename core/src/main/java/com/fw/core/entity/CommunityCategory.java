package com.fw.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class CommunityCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @TableField("categoryName")
    private String categoryName;


}

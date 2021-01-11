package com.fw.admin.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yqf
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFile implements Serializable {
    private Integer id;
    private String filePath;
    private String fileName;
    private Date upTime;

    public UploadFile(String filePath, String fileName, Date upTime) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.upTime = upTime;
    }
}
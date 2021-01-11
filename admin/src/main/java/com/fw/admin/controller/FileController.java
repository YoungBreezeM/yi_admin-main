package com.fw.admin.controller;


import com.fw.admin.entity.FileProperties;
import com.fw.admin.utils.FileUtil;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * @author yqf
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileProperties fileProperties;

    /**
     * 单文件上传
     * */
    @PostMapping
    public ResponseEntity<Result> uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {

        System.out.println(file.getBytes());
        System.out.println(file.getContentType());
        System.out.println(file.getName());
        System.out.println(file.getOriginalFilename());

        FileUtil images = new FileUtil(fileProperties, "images");

        if (!file.isEmpty()) {
            String url = images.storeFile(file);

            if (url!=null){


                return new ResponseEntity<>(new Result(ResultType.UPLOAD_SUCCESS,url), HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(new Result(ResultType.UPLOAD_FAIL), HttpStatus.OK);

    }

}

package com.fw.wx.controller;

import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.wx.domain.FileProperties;
import com.fw.wx.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author yqf
 * @date 2020/11/20 下午3:29
 */
@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private FileProperties fileProperties;
    /**
     * 单文件上传
     * */
    @PostMapping("/image")
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

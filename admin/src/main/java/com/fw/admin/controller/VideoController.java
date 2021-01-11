package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.admin.entity.FileProperties;
import com.fw.admin.utils.FileUtil;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Video;
import com.fw.core.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    FileProperties fileProperties;
    @Autowired 
    private VideoService videoService;
    
    @PostMapping
    public ResponseEntity<Result> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

        FileUtil video = new FileUtil(fileProperties, "video");

        if (!file.isEmpty()) {
            String url = video.storeFile(file);
            if (url!=null){
                Video video1 = new Video();
                video1.setVideoUrl(url);
                video1.setVideoName(System.currentTimeMillis()+"_"+file.getOriginalFilename());
                video1.setType(file.getContentType());
                video1.setSize(file.getSize());
                video1.setCreatedTime(new Date());
                boolean s = videoService.save(video1);

                if(s){
                    return new ResponseEntity<>(new Result(ResultType.UPLOAD_SUCCESS,video1), HttpStatus.OK);
                }

            }
        }

        return new ResponseEntity<>(new Result(ResultType.UPLOAD_FAIL), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> imageListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){


        IPage<Video> page = new Page<>(startNum, pageSize);
        QueryWrapper<Video> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<Video> page1 = videoService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}/{videoCategoryId}")
    public ResponseEntity<Result> imageListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable Integer videoCategoryId){


        IPage<Video> page = new Page<>(startNum, pageSize);
        QueryWrapper<Video> objectQueryWrapper = new QueryWrapper<>();
        Video video = new Video(); video.setVideoCategoryId(videoCategoryId);
        objectQueryWrapper.setEntity(video);
        IPage<Video> pages = videoService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",pages.getRecords());
        rs.put("total",pages.getTotal());
        rs.put("size",pages.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Result> updateVideo(@RequestBody Video video){
        boolean b = videoService.updateById(video);

        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ResponseEntity<Result> deleteVideoCategory(@PathVariable Integer id){
        Video video = videoService.getById(id);
        FileUtil fileUtil = new FileUtil();
        Boolean aBoolean = fileUtil.deleteFile(video.getVideoUrl());

        if(aBoolean){
            boolean b = videoService.removeById(id);
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


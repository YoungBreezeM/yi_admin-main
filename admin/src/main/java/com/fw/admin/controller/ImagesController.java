package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.admin.entity.FileProperties;
import com.fw.admin.utils.FileUtil;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.BaseGua;
import com.fw.core.entity.Images;
import com.fw.core.service.ImagesService;
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
@RequestMapping("/images")
public class ImagesController {

    @Autowired
    FileProperties fileProperties;
    @Autowired
    private ImagesService imagesService;

    @PostMapping
    public ResponseEntity<Result> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {

        FileUtil images = new FileUtil(fileProperties, "images");

        if (!file.isEmpty()) {
            String url = images.storeFile(file);
            if (url!=null){
                Images images1 = new Images();
                images1.setUrl(url);
                images1.setName(file.getOriginalFilename()+System.currentTimeMillis());
                images1.setType(file.getContentType());
                images1.setMagnitude(file.getSize());
                images1.setUploadTime(new Date());
                boolean s = imagesService.save(images1);

                if(s){
                    return new ResponseEntity<>(new Result(ResultType.UPLOAD_SUCCESS,images1), HttpStatus.OK);
                }

            }
        }

        return new ResponseEntity<>(new Result(ResultType.UPLOAD_FAIL), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> imageListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){


        IPage<Images> page = new Page<>(startNum, pageSize);
        QueryWrapper<Images> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<Images> page1 = imagesService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}/{imagesCategoryId}")
    public ResponseEntity<Result> imageListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable Integer imagesCategoryId){


        IPage<Images> page = new Page<>(startNum, pageSize);
        QueryWrapper<Images> objectQueryWrapper = new QueryWrapper<>();
        Images images = new Images(); images.setCategoryId(imagesCategoryId);
        objectQueryWrapper.setEntity(images);
        IPage<Images> pages = imagesService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",pages.getRecords());
        rs.put("total",pages.getTotal());
        rs.put("size",pages.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<Result> updateImage(@RequestBody Images images){
        boolean b = imagesService.updateById(images);

        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ResponseEntity<Result> deleteImagesCategory(@PathVariable Integer id){
        Images images = imagesService.getById(id);
        FileUtil fileUtil = new FileUtil();
        Boolean aBoolean = fileUtil.deleteFile(images.getUrl());

        if(aBoolean){
            boolean b = imagesService.removeById(id);
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}



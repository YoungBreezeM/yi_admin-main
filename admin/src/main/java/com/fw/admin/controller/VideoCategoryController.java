package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.VideoCategory;
import com.fw.core.service.VideoCategoryService;
import com.fw.core.service.VideoCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
@RequestMapping("/videoCategory")
public class VideoCategoryController {
    @Autowired
    private VideoCategoryService videoCategoryService;
    @GetMapping
    public ResponseEntity<Result> videoCategoryList(){

        List<VideoCategory> list = videoCategoryService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> videoCategoryListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<VideoCategory> page = new Page<>(startNum, pageSize);
        QueryWrapper<VideoCategory> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<VideoCategory> page1 = videoCategoryService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Result> updateOneVideoCategory(@RequestBody VideoCategory videoCategory){
        System.out.println(videoCategory);
        if (videoCategory!=null){
            boolean update = videoCategoryService.updateById(videoCategory);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addVideoCategory(@RequestBody VideoCategory videoCategory){
        if (videoCategory!=null){
            boolean save = videoCategoryService.save(videoCategory);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteVideoCategory(@PathVariable Integer id){
        boolean b = videoCategoryService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


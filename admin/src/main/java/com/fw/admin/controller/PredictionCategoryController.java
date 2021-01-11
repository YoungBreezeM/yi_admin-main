package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.service.PredictionCategoryService;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.PredictionCategory;
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
@RequestMapping("/predictionCategory")
public class PredictionCategoryController {

    @Autowired
    private PredictionCategoryService predictionCategoryService;
    @GetMapping
    public ResponseEntity<Result> predictionCategoryList(){

        List<PredictionCategory> list = predictionCategoryService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> predictionCategoryListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<PredictionCategory> page = new Page<>(startNum, pageSize);
        QueryWrapper<PredictionCategory> objectQueryWrapper = new QueryWrapper<>();

        IPage<PredictionCategory> page1 = predictionCategoryService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> updateOneGua(@RequestBody PredictionCategory predictionCategory){

        if (predictionCategory!=null){
            boolean update = predictionCategoryService.updateById(predictionCategory);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addBaseGua(@RequestBody PredictionCategory predictionCategory){
        if (predictionCategory!=null){
            boolean save = predictionCategoryService.save(predictionCategory);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteBaseGua(@PathVariable Integer id){
        boolean b = predictionCategoryService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


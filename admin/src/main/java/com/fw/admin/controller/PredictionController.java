package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.service.PredictionService;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Prediction;
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
@RequestMapping("/prediction")
public class PredictionController {
    @Autowired
    private PredictionService predictionService;
    @GetMapping
    public ResponseEntity<Result> predictionList(){

        List<Prediction> list = predictionService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}/{guaId}/{categoryId}")
    public ResponseEntity<Result> predictionListPageByCidAndGid(@PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable Integer guaId, @PathVariable Integer categoryId){

        IPage<Prediction> page = new Page<>(startNum, pageSize);
        QueryWrapper<Prediction> objectQueryWrapper = new QueryWrapper<>();
        Prediction prediction = new Prediction();
        prediction.setCategoryId(categoryId);
        prediction.setBaseGuaId(guaId);
        objectQueryWrapper.setEntity(prediction);

        IPage<Prediction> page1 = predictionService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> predictionListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<Prediction> page = new Page<>(startNum, pageSize);
        QueryWrapper<Prediction> objectQueryWrapper = new QueryWrapper<>();

        IPage<Prediction> page1 = predictionService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> update(@RequestBody Prediction prediction){

        if (prediction!=null){
            boolean update = predictionService.updateById(prediction);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addBaseGua(@RequestBody Prediction prediction){
        if (prediction!=null){
            boolean save = predictionService.save(prediction);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteBaseGua(@PathVariable Integer id){
        boolean b = predictionService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


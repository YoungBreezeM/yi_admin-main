package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.service.BaseGuaService;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.BaseGua;
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
@RequestMapping("/baseGua")
public class BaseGuaController {
    @Autowired
    private BaseGuaService baseGuaService;

    @GetMapping
    public ResponseEntity<Result> baseGuaList(){

        List<BaseGua> list = baseGuaService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> baseGuaListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<BaseGua> page = new Page<>(startNum, pageSize);
        QueryWrapper<BaseGua> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<BaseGua> page1 = baseGuaService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}/{guaName}")
    public ResponseEntity<Result> baseGuaListPageByName(@PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable String guaName){

        IPage<BaseGua> page = new Page<>(startNum, pageSize);
        QueryWrapper<BaseGua> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper
                .like("guaName",guaName);

        IPage<BaseGua> page1 = baseGuaService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> updateOneGua(@RequestBody BaseGua baseGua){
        System.out.println(baseGua);
        if (baseGua!=null){
            boolean update = baseGuaService.updateById(baseGua);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addBaseGua(@RequestBody BaseGua baseGua){
        if (baseGua!=null){
            boolean save = baseGuaService.save(baseGua);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteBaseGua(@PathVariable Integer id){
        boolean b = baseGuaService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


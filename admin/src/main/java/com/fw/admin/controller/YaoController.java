package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.service.YaoService;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;

import com.fw.core.entity.Yao;
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
@RequestMapping("/yao")
public class YaoController {

    @Autowired
    private YaoService yaoService;

    @GetMapping
    public ResponseEntity<Result> baseYaoList(){

        List<Yao> list = yaoService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("{baseGuaId}")
    public ResponseEntity<Result> getYaoByBaseGuaId(@PathVariable Integer baseGuaId){
        QueryWrapper<Yao> yaoQueryWrapper = new QueryWrapper<>();
        Yao yao = new Yao();
        yao.setBaseGuaId(baseGuaId);
        yaoQueryWrapper.setEntity(yao);
        List<Yao> list = yaoService.list(yaoQueryWrapper);
        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list),HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> yaoListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<Yao> page = new Page<>(startNum, pageSize);
        QueryWrapper<Yao> objectQueryWrapper = new QueryWrapper<>();

        IPage<Yao> page1 = yaoService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}/{yaoName}")
    public ResponseEntity<Result> yaoListPageByName(@PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable String yaoName){

        IPage<Yao> page = new Page<>(startNum, pageSize);
        QueryWrapper<Yao> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper
                .like("yaoName",yaoName);
        IPage<Yao> page1 = yaoService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> updateOneGua(@RequestBody Yao yao){

        if (yao!=null){
            boolean update = yaoService.updateById(yao);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addBaseGua(@RequestBody Yao yao){
        if (yao!=null){
            boolean save = yaoService.save(yao);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteBaseGua(@PathVariable Integer id){
        boolean b = yaoService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


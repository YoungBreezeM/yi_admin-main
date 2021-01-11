package com.fw.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Links;
import com.fw.core.service.LinksService;
import com.fw.core.service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yqf
 * @date 2020/11/28 下午8:32
 */
@RestController
@RequestMapping("/links")
public class LinksController {

    @Autowired
    private LinksService linksService;

    
    @GetMapping
    public ResponseEntity<Result> linksList(){

        List<Links> list = linksService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> linksListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<Links> page = new Page<>(startNum, pageSize);
        QueryWrapper<Links> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<Links> page1 = linksService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<Result> updateOneLinks(@RequestBody Links links){
        System.out.println(links);
        if (links!=null){
            boolean update = linksService.updateById(links);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addLinks(@RequestBody Links links){
        if (links!=null){
            boolean save = linksService.save(links);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteLinks(@PathVariable Integer id){
        boolean b = linksService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}

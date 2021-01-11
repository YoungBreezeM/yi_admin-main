package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.BaseGua;
import com.fw.core.entity.CommunityCategory;
import com.fw.core.service.CommunityCategoryService;
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
@RequestMapping("/communityCategory")
public class CommunityCategoryController {
    @Autowired
    private CommunityCategoryService service;

    @GetMapping
    public ResponseEntity<Result> getCommunityCategoryList(){
        List<CommunityCategory> list = service.list();
        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> communityListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<CommunityCategory> page = new Page<>(startNum, pageSize);
        QueryWrapper<CommunityCategory> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<CommunityCategory> page1 = service.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}/{categoryName}")
    public ResponseEntity<Result> communityListPageByName(@PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable String categoryName){

        IPage<CommunityCategory> page = new Page<>(startNum, pageSize);
        QueryWrapper<CommunityCategory> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper
                .like("categoryName",categoryName);

        IPage<CommunityCategory> page1 = service.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> updateOneCommunity(@RequestBody CommunityCategory communityCategory){

        if (communityCategory!=null){
            boolean update = service.updateById(communityCategory);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addBaseGua(@RequestBody CommunityCategory communityCategory){
        if (communityCategory!=null){
            boolean save = service.save(communityCategory);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteBaseGua(@PathVariable Integer id){
        boolean b = service.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


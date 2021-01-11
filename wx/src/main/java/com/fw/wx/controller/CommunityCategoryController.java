package com.fw.wx.controller;


import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
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


}


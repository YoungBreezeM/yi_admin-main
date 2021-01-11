package com.fw.wx.controller;

import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Links;
import com.fw.core.service.LinksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yqf
 * @date 2020/11/28 下午9:13
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
}

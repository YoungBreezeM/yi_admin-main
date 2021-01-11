package com.fw.admin.controller;


import com.fw.core.service.GuaService;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Gua;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
@RestController
@RequestMapping("/gua")
public class GuaController {

    @Autowired
    private GuaService guaService;
    @GetMapping
    public ResponseEntity<Result> getGuaList(){
        List<Gua> list = guaService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }
}


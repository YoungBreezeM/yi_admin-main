package com.fw.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.entity.Client;
import com.fw.core.entity.Grade;
import com.fw.core.entity.Prediction;
import com.fw.core.entity.PredictionCategory;
import com.fw.core.service.ClientService;
import com.fw.core.service.GradeService;
import com.fw.core.service.PredictionCategoryService;
import com.fw.core.service.PredictionService;
import com.fw.wx.domain.ClientPrediction;
import com.fw.wx.domain.WxRes;
import com.fw.wx.domain.WxResType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqf
 * @date 2020/11/13 上午10:02
 */
@RestController
@RequestMapping("/prediction")
public class PredictionController {

    @Autowired
    private PredictionCategoryService predictionCategoryService;
    @Autowired
    private PredictionService predictionService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private GradeService gradeService;

    @GetMapping("/category")
    public ResponseEntity<WxRes> findPredictionCategory(){
        List<PredictionCategory> list = predictionCategoryService.list();

        return  new ResponseEntity<>(new WxRes(WxResType.SUCCESS,list), HttpStatus.OK);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<WxRes> findPredictionText(@RequestBody ClientPrediction clientPrediction){
        Client client = clientPrediction.getClient();
        Client byId = clientService.getById(client.getId());
        QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();
        gradeQueryWrapper.lt("min",byId.getIntegral());
        gradeQueryWrapper.ge("max",byId.getIntegral());
        List<Grade> grades = gradeService.list(gradeQueryWrapper);
        byId.setIntegral(byId.getIntegral()-grades.get(0).getValue());
        clientService.updateById(byId);

        QueryWrapper<Prediction> predictionQueryWrapper = new QueryWrapper<>();

        predictionQueryWrapper.setEntity(clientPrediction.getPrediction());

        List<Prediction> list = predictionService.list(predictionQueryWrapper);



        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,list.get(0)),HttpStatus.OK);
    }
}

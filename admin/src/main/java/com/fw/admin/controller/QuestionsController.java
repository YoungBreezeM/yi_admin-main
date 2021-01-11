package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Questions;
import com.fw.core.entity.Questions;
import com.fw.core.entity.WxImages;
import com.fw.core.service.QuestionsService;
import com.fw.core.service.QuestionsService;
import com.fw.core.service.WxImagesService;
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
@RequestMapping("/questions")
public class QuestionsController {
    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private WxImagesService wxImagesService;
    @GetMapping
    public ResponseEntity<Result> questionsList(){

        List<Questions> list = questionsService.list();

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}/{categoryId}")
    public ResponseEntity<Result> imageListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable Integer categoryId){


        IPage<Questions> page = new Page<>(startNum, pageSize);
        QueryWrapper<Questions> objectQueryWrapper = new QueryWrapper<>();
        Questions questions = new Questions(); questions.setCategoryId(categoryId);
        objectQueryWrapper.setEntity(questions);
        IPage<Questions> pages = questionsService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",pages.getRecords());
        rs.put("total",pages.getTotal());
        rs.put("size",pages.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("/wxImages/{questionId}")
    public ResponseEntity<Result> getQuestionsImagesById(@PathVariable Integer questionId){

        WxImages wxImages = new WxImages();
        wxImages.setQuestionId(questionId);
        QueryWrapper<WxImages> wxImagesQueryWrapper = new QueryWrapper<>();
        wxImagesQueryWrapper.setEntity(wxImages);
        List<WxImages> list = wxImagesService.list(wxImagesQueryWrapper);
        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list),HttpStatus.OK);
    }

    @GetMapping("/page/{startNum}/{pageSize}")
    public ResponseEntity<Result> questionsListPage(@PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<Questions> page = new Page<>(startNum, pageSize);
        QueryWrapper<Questions> objectQueryWrapper = new QueryWrapper<>();
        System.out.println(startNum+":"+pageSize);

        IPage<Questions> page1 = questionsService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",page1.getRecords());
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new Result(ResultType.SUCCESS,rs), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Result> updateOneQuestions(@RequestBody Questions questions){
        System.out.println(questions);
        if (questions!=null){
            boolean update = questionsService.updateById(questions);

            if(update){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Result> addQuestions(@RequestBody Questions questions){
        if (questions!=null){
            boolean save = questionsService.save(questions);
            if(save){
                return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
            }
            return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
        }

        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteQuestions(@PathVariable Integer id){
        boolean b = questionsService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


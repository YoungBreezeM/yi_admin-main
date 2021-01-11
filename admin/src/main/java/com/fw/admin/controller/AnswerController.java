package com.fw.admin.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.domain.Result;
import com.fw.core.domain.ResultType;
import com.fw.core.entity.Answer;
import com.fw.core.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/answer")
public class AnswerController {
    @Autowired
    private AnswerService answerService;

    @GetMapping("{questionId}")
    public ResponseEntity<Result> getAnswerByQuestionId(@PathVariable Integer questionId){
        Answer answer = new Answer();
        answer.setQuestionsId(questionId);
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        answerQueryWrapper.setEntity(answer);
        List<Answer> list = answerService.list(answerQueryWrapper);
        return new ResponseEntity<>(new Result(ResultType.SUCCESS,list), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Result> deleteAnswer(@PathVariable Integer id){
        boolean b = answerService.removeById(id);
        if(b){
            return new ResponseEntity<>(new Result(ResultType.SUCCESS),HttpStatus.OK);
        }
        return new ResponseEntity<>(new Result(ResultType.FAIL),HttpStatus.OK);
    }
}


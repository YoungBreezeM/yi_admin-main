package com.fw.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.entity.*;
import com.fw.core.service.AnswerService;
import com.fw.core.service.ClientService;
import com.fw.core.service.QuestionsService;
import com.fw.core.service.WxImagesService;
import com.fw.wx.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author yqf
 * @date 2020/11/18 下午8:13
 */
@RestController
@RequestMapping("/question")
public class QuestionsController {

    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private WxImagesService wxImagesService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AnswerService answerService;
    
    @GetMapping("{categoryId}/{startNum}/{pageSize}")
    public ResponseEntity<WxRes> getQuestionList(@PathVariable Integer categoryId, @PathVariable Integer startNum, @PathVariable Integer pageSize){

        IPage<Questions> page = new Page<>(startNum, pageSize);
        QueryWrapper<Questions> objectQueryWrapper = new QueryWrapper<>();
        Questions questions = new Questions();
        questions.setCategoryId(categoryId);
        objectQueryWrapper.setEntity(questions);
        objectQueryWrapper.orderByDesc("time");
        IPage<Questions> page1 = questionsService.page(page, objectQueryWrapper);

        List<QuestionClient> questionClientList = new ArrayList<>();

        for (Questions record : page1.getRecords()) {
            List<String> imgList = new ArrayList<>();
            QueryWrapper<WxImages> wxImagesQueryWrapper = new QueryWrapper<>();
            WxImages wxImages = new WxImages();
            wxImages.setQuestionId(record.getId());
            wxImagesQueryWrapper.setEntity(wxImages);
            List<WxImages> list = wxImagesService.list(wxImagesQueryWrapper);
            if (list!=null){
                for (WxImages images : list) {
                    imgList.add(images.getUrl());
                }
            }
            QuestionClient questionClient = new QuestionClient();
            Client client = clientService.getById(record.getClientId());
            questionClient.setClient(client);
            questionClient.setQuestions(record);
            questionClient.setImgList(imgList);
            questionClientList.add(questionClient);
        }

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",questionClientList);
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());


        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,rs),HttpStatus.OK);
    }

    @PutMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ResponseEntity<WxRes> addQuestion(@RequestBody QuestionClient questionClient){

        System.out.println(questionClient);
        Questions questions = questionClient.getQuestions();
        Client client = questionClient.getClient();
        List<String> imgList = questionClient.getImgList();
        questions.setTime(new Date());
        WxImages wxImages = new WxImages();

        //更新客户端
        clientService.updateById(client);

        boolean save = questionsService.save(questions);
        if(save){

            for (String url : imgList) {
                wxImages.setQuestionId(questions.getId());
                wxImages.setUrl(url);
                wxImages.setUploadTime(new Date());
                wxImagesService.save(wxImages);
            }

            return new ResponseEntity<>(new WxRes(WxResType.SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<>(new WxRes(WxResType.FAIL), HttpStatus.OK);
    }

    @PostMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ResponseEntity<WxRes> updateQuestion(@RequestBody QuestionClient questionClient){

        System.out.println(questionClient);
        Questions questions = questionClient.getQuestions();
        Client client = questionClient.getClient();
        List<String> imgList = questionClient.getImgList();
        questions.setTime(new Date());
        WxImages wxImages = new WxImages();

        //更新客户端
        clientService.updateById(client);

        boolean update = questionsService.updateById(questions);
        if(update){
            wxImages.setQuestionId(questions.getId());
            QueryWrapper<WxImages> wxImagesQueryWrapper = new QueryWrapper<>();
            wxImagesQueryWrapper.setEntity(wxImages);
            wxImagesService.remove(wxImagesQueryWrapper);

            for (String url : imgList) {
                wxImages.setUrl(url);
                wxImages.setUploadTime(new Date());
                wxImagesService.save(wxImages);
            }

            return new ResponseEntity<>(new WxRes(WxResType.SUCCESS), HttpStatus.OK);
        }
        return new ResponseEntity<>(new WxRes(WxResType.FAIL), HttpStatus.OK);
    }
    @GetMapping("{questionId}")
    public ResponseEntity<WxRes> getQuestionAnswer(@PathVariable Integer questionId){
        Questions questions = questionsService.getById(questionId);
        if (questions.getWatch()!=null){
            questions.setWatch(questions.getWatch()+1);
        }else {
            questions.setWatch(1);
        }

        questionsService.updateById(questions);
        Client client = clientService.getById(questions.getClientId());

        QueryWrapper<WxImages> questionsQueryWrapper = new QueryWrapper<>();
        WxImages wxImages1 = new WxImages();
        wxImages1.setQuestionId(questions.getId());
        questionsQueryWrapper.setEntity(wxImages1);
        List<WxImages> list = wxImagesService.list(questionsQueryWrapper);
        List<String> imgList = new ArrayList<>();
        for (WxImages wxImages : list) {
            imgList.add(wxImages.getUrl());
        }

        QuestionClient questionClient = new QuestionClient();
        questionClient.setQuestions(questions);
        questionClient.setClient(client);
        questionClient.setImgList(imgList);

        //获取问题回答list
        QueryWrapper<Answer> answerQueryWrapper = new QueryWrapper<>();
        Answer answer1 = new Answer();
        answer1.setQuestionsId(questions.getId());
        answerQueryWrapper.setEntity(answer1);
        List<Answer> answers = answerService.list(answerQueryWrapper);
        List<AnswerClient> answerClients = new ArrayList<>();
        for (Answer answer : answers) {
            AnswerClient answerClient = new AnswerClient();
            Client byId = clientService.getById(answer.getClientId());
            answerClient.setAnswer(answer);
            answerClient.setClient(byId);
            answerClients.add(answerClient);
        }

        QuestionAnswers questionAnswers = new QuestionAnswers();
        questionAnswers.setAnswerClients(answerClients);
        questionAnswers.setQuestionClient(questionClient);

        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,questionAnswers),HttpStatus.OK);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<WxRes> deleteQuestion(@PathVariable Integer id){
        boolean b = questionsService.removeById(id);

        if(b){
            return new ResponseEntity<>(new WxRes(WxResType.SUCCESS),HttpStatus.OK);
        }

        return new ResponseEntity<>(new WxRes(WxResType.FAIL),HttpStatus.OK);
    }
}

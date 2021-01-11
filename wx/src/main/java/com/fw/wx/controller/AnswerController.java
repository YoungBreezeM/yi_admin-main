package com.fw.wx.controller;

import com.fw.core.entity.Answer;
import com.fw.core.entity.Client;
import com.fw.core.entity.Message;
import com.fw.core.entity.Questions;
import com.fw.core.service.AnswerService;
import com.fw.core.service.ClientService;
import com.fw.core.service.MessageService;
import com.fw.core.service.QuestionsService;
import com.fw.wx.domain.AnswerClient;
import com.fw.wx.domain.WxRes;
import com.fw.wx.domain.WxResType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author yqf
 * @date 2020/11/21 下午7:48
 */
@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    private AnswerService answerService;
    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private MessageService messageService;

    @PutMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ResponseEntity<WxRes> addAnswerByQuestionId(@RequestBody  AnswerClient answerClient){
        Answer answer = answerClient.getAnswer();
        Client client = answerClient.getClient();
        Questions questions = answerClient.getQuestions();
        System.out.println(answerClient);

        answer.setTime(new Date());
        answerService.save(answer);

        if(questions.getIntegral()>0){
            questions.setIntegral(questions.getIntegral()-1);
            client.setIntegral(client.getIntegral()+1);
        }

        questionsService.updateById(questions);
        clientService.updateById(client);
        Message message = new Message();
        message.setAnswerId(answer.getId());
        message.setQuestionId(questions.getId());
        message.setStatus(false);
        message.setTime(new Date());
        messageService.save(message);
        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS), HttpStatus.OK);
    }
}

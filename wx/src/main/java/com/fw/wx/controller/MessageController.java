package com.fw.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.entity.*;
import com.fw.core.service.*;
import com.fw.wx.domain.MessageClient;
import com.fw.wx.domain.QuestionClient;
import com.fw.wx.domain.WxRes;
import com.fw.wx.domain.WxResType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yqf
 * @date 2020/11/30 下午2:42
 */
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private WxImagesService wxImagesService;
    @Autowired
    private AnswerService answerService;

    @GetMapping("/count/{clientId}")
    public ResponseEntity<WxRes> getNewMessageCount(@PathVariable Integer clientId){
        Questions questions = new Questions();questions.setClientId(clientId);
        QueryWrapper<Questions> questionsQueryWrapper = new QueryWrapper<>();
        questionsQueryWrapper.setEntity(questions);
        List<Questions> list = questionsService.list(questionsQueryWrapper);
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        int count = 0;
        for (Questions questions1 : list) {
            Message message = new Message();
            message.setQuestionId(questions1.getId());
            message.setStatus(false);
            messageQueryWrapper.setEntity(message);
            count += messageService.count(messageQueryWrapper);

        }
        Map<String,Integer> rs = new HashMap<>(10);rs.put("count",count);
        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,rs), HttpStatus.OK);
    }

    @GetMapping("{clientId}")
    public ResponseEntity<WxRes> getQuestionList(@PathVariable Integer clientId){

        Questions questions = new Questions();
        questions.setClientId(clientId);
        QueryWrapper<Questions> questionsQueryWrapper = new QueryWrapper<>();
        questionsQueryWrapper.setEntity(questions);
        List<Questions> list = questionsService.list(questionsQueryWrapper);
        QueryWrapper<Message> messageQueryWrapper = new QueryWrapper<>();
        List<Message> messages = new ArrayList<>();
        List<Message> newMessage = new ArrayList<>();
        List<Message> oldMessage = new ArrayList<>();
        for (Questions questions1 : list) {
            Message message = new Message();
            message.setQuestionId(questions1.getId());
            messageQueryWrapper.setEntity(message);
            messages.addAll(messageService.list(messageQueryWrapper));
        }

        for (Message message : messages) {
            if(message.getStatus()){
                oldMessage.add(message);
            }else {
                Message message1 = new Message();
                message1.setId(message.getId());
                message1.setQuestionId(message.getQuestionId());
                message1.setStatus(true);
                message1.setAnswerId(message.getAnswerId());
                messageQueryWrapper.setEntity(message1);
                messageService.updateById(message1);
                newMessage.add(message);
            }

        }

        oldMessage = oldMessage.stream().sorted(Comparator.comparing(Message::getTime).reversed()).collect(Collectors.toList());//根据创建时间倒排
        newMessage = newMessage.stream().sorted(Comparator.comparing(Message::getTime).reversed()).collect(Collectors.toList());//根据创建时间倒排

        messages.clear();
        messages.addAll(newMessage);
        messages.addAll(oldMessage);

        List<MessageClient> messageClients= new ArrayList<>();
        QueryWrapper<WxImages> wxImagesQueryWrapper = new QueryWrapper<>();

        for (Message message : messages) {
            MessageClient messageClient = new MessageClient();
            messageClient.setStatus(message.getStatus());

            Answer answer = answerService.getById(message.getAnswerId());
            messageClient.setAnswer(answer);

            Client client = clientService.getById(answer.getClientId());
            messageClient.setClient(client);

            Questions questions1 = questionsService.getById(message.getQuestionId());
            messageClient.setQuestions(questions1);

            WxImages wxImages = new WxImages();wxImages.setQuestionId(questions1.getId());
            wxImagesQueryWrapper.setEntity(wxImages);
            List<WxImages> wxImagesList = wxImagesService.list(wxImagesQueryWrapper);
            messageClient.setUrl(wxImagesList.get(0).getUrl());

            messageClients.add(messageClient);
        }

        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,messageClients),HttpStatus.OK);
    }


}

package com.fw.wx;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.entity.*;
import com.fw.core.service.*;
import com.fw.wx.domain.MessageClient;
import com.fw.wx.domain.QuestionClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class WxApplicationTests {
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
    @Autowired
    private GradeService gradeService;

    @Test
    void contextLoads() throws CloneNotSupportedException {
        Questions questions = new Questions();
        questions.setClientId(24);
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
        System.out.println(messageClients);
    }

    @Test
     void testGrade(){
        QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();

        gradeQueryWrapper.lt("min",105);
        gradeQueryWrapper.ge("max",105);
        List<Grade> list = gradeService.list(gradeQueryWrapper);
        System.out.println(list);
    }

}

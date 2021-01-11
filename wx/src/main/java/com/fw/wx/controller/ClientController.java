package com.fw.wx.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.entity.*;
import com.fw.core.service.*;
import com.fw.core.utils.TokenUtil;
import com.fw.wx.domain.QuestionClient;
import com.fw.wx.domain.WxClient;
import com.fw.wx.domain.WxRes;
import com.fw.wx.domain.WxResType;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.table.TableCellRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yqf
 * @date 2020/11/11 下午3:37
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private WxMaService wxMaService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private QuestionsService questionsService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private WxImagesService wxImagesService;
    @Autowired
    private AttentionService attentionService;
    @Autowired
    private GradeService gradeService;

    @GetMapping("/login/{code}")
    public ResponseEntity<WxRes> login(@PathVariable String code) throws WxErrorException {

        WxMaJscode2SessionResult sessionInfo = wxMaService.getUserService().getSessionInfo(code);

        if(sessionInfo.getOpenid()==null){
            return new ResponseEntity<>(new WxRes(WxResType.LOGIN_FAIL), HttpStatus.OK);
        }

        Client client = new Client();
        client.setOpenId(sessionInfo.getOpenid());

        QueryWrapper<Client> clientQueryWrapper = new QueryWrapper<>();
        clientQueryWrapper.setEntity(client);
        Client one = clientService.getOne(clientQueryWrapper);

        if(one==null){
            client.setIntegral(100);
            client.setStatus(true);
            clientService.save(client);
            return new ResponseEntity<>(new WxRes(WxResType.LOGIN_SUCCESS,client), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new WxRes(WxResType.LOGIN_SUCCESS,one), HttpStatus.OK);
        }


    }

    @PostMapping("/login/setUserInfo")
    public ResponseEntity<WxRes> setUserInfo(@RequestBody  Client client){

        System.out.println(client);
        Client byId = clientService.getById(client.getId());
        if(byId.getNickName()==null){
            clientService.updateById(client);
        }
        String sign = tokenUtil.sign(client);
        Map<String,String> token = new HashMap<>(10);token.put("token",sign);
        return new ResponseEntity<>(new WxRes(WxResType.LOGIN_SUCCESS,token), HttpStatus.OK);
    }

    @GetMapping("/userInfo")
    public ResponseEntity<WxRes> getUserInfo(HttpServletRequest request){
        String token = request.getHeader("API-Authorization");
        if(token==null){
            return new ResponseEntity<>(new WxRes(WxResType.TOKEN_VERIFY_FAIL),HttpStatus.OK);
        }

        WxClient verify = tokenUtil.verify(token, WxClient.class);
        if(verify==null){
            return new ResponseEntity<>(new WxRes(WxResType.TOKEN_VERIFY_FAIL),HttpStatus.OK);
        }
        Client client = new Client();
        client.setOpenId(verify.getOpenid());
        QueryWrapper<Client> clientQueryWrapper = new QueryWrapper<>();
        clientQueryWrapper.setEntity(client);
        Client one = clientService.getOne(clientQueryWrapper);

        QueryWrapper<Grade> gradeQueryWrapper = new QueryWrapper<>();
        gradeQueryWrapper.lt("min",one.getIntegral());
        gradeQueryWrapper.ge("max",one.getIntegral());
        List<Grade> list = gradeService.list(gradeQueryWrapper);
        Map<String,Object> rs = new HashMap<>(10);
        rs.put("client",one);
        rs.put("grade",list.get(0));
        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,rs),HttpStatus.OK);
    }

    @GetMapping("/questions/{fromId}/{clientId}/{startNum}/{pageSize}")
    public ResponseEntity<WxRes> getQuestionsByClientId(@PathVariable Integer clientId, @PathVariable Integer startNum, @PathVariable Integer pageSize, @PathVariable Integer fromId){
        Client client = clientService.getById(clientId);

        IPage<Questions> page = new Page<>(startNum, pageSize);
        QueryWrapper<Questions> objectQueryWrapper = new QueryWrapper<>();
        Questions questions = new Questions();
        questions.setClientId(client.getId());
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
            questionClient.setClient(client);
            questionClient.setQuestions(record);
            questionClient.setImgList(imgList);
            questionClientList.add(questionClient);
        }

        Attention attention = new Attention();
        attention.setFromId(fromId);
        attention.setToId(client.getId());
        QueryWrapper<Attention> attentionQueryWrapper = new QueryWrapper<>();
        attentionQueryWrapper.setEntity(attention);
        Attention one = attentionService.getOne(attentionQueryWrapper);


        Map<String,Object> rs = new HashMap<>(10);
        if(one!=null){
            rs.put("attention",true);
        }else {
            rs.put("attention",false);
        }
        rs.put("client",client);
        rs.put("list",questionClientList);
        rs.put("total",page1.getTotal());
        rs.put("size",page1.getSize());

        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,rs),HttpStatus.OK);

    }
}

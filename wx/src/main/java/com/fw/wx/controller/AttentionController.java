package com.fw.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.entity.Attention;
import com.fw.core.entity.Client;
import com.fw.core.service.AttentionService;
import com.fw.core.service.ClientService;
import com.fw.wx.domain.WxRes;
import com.fw.wx.domain.WxResType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yqf
 * @date 2020/11/22 下午7:38
 */
@RestController
@RequestMapping("/attention")
public class AttentionController  {

    @Autowired
    private AttentionService attentionService;
    @Autowired
    private ClientService clientService;

    @PostMapping
    public ResponseEntity<WxRes> addAttention(@RequestBody Attention attention){
        QueryWrapper<Attention> objectQueryWrapper = new QueryWrapper<>();
        objectQueryWrapper.setEntity(attention);
        Attention one = attentionService.getOne(objectQueryWrapper);

        if(one!=null){
            attentionService.removeById(one.getId());
        }else{
            attentionService.save(attention);
        }

        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS), HttpStatus.OK);
    }

    @GetMapping("{clientId}")
    public ResponseEntity<WxRes> getAttentionById(@PathVariable Integer clientId){
        QueryWrapper<Attention> attentionQueryWrapper = new QueryWrapper<>();
        Attention attention = new Attention();
        attention.setFromId(clientId);
        List<Attention> list = attentionService.list(attentionQueryWrapper);
        List<Client> clients = new ArrayList<>();

        for (Attention attention1 : list) {
            if(!attention1.getFromId().equals(attention1.getId())){
                QueryWrapper<Client> clientQueryWrapper = new QueryWrapper<>();
                Client client = new Client();client.setId(attention1.getToId());
                clientQueryWrapper.setEntity(client);
                Client one = clientService.getOne(clientQueryWrapper);
                if(one!=null){
                    clients.add(one);
                }

            }
        }
        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,clients),HttpStatus.OK);
    }
}

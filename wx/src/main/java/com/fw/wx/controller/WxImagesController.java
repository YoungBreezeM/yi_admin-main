package com.fw.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.entity.WxImages;
import com.fw.core.service.WxImagesService;
import com.fw.wx.domain.FileProperties;
import com.fw.wx.domain.WxRes;
import com.fw.wx.domain.WxResType;
import com.fw.wx.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yqf
 * @date 2020/11/29 下午8:52
 */
@RestController
@RequestMapping("/wxImages")
public class WxImagesController {
    @Autowired
    private WxImagesService wxImagesService;
    @Autowired
    private FileProperties fileProperties;

    @DeleteMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public ResponseEntity<WxRes> deleteWxImages(@RequestBody WxImages wxImages){
        QueryWrapper<WxImages> wxImagesQueryWrapper = new QueryWrapper<>();
        wxImagesQueryWrapper.setEntity(wxImages);
        FileUtil fileUtil = new FileUtil();
        Boolean aBoolean = fileUtil.deleteFile(wxImages.getUrl());
        boolean remove = wxImagesService.remove(wxImagesQueryWrapper);
        if (remove&&aBoolean){
            return new ResponseEntity<>(new WxRes(WxResType.SUCCESS), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new WxRes(WxResType.FAIL),HttpStatus.OK);
        }
    }
}

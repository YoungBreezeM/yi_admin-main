package com.fw.wx.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.entity.BaseGua;
import com.fw.core.entity.Gua;
import com.fw.core.entity.Yao;
import com.fw.core.service.BaseGuaService;
import com.fw.core.service.GuaService;
import com.fw.core.service.YaoService;
import com.fw.wx.domain.ComputeGua;
import com.fw.wx.domain.WxRes;
import com.fw.wx.domain.WxResType;
import com.fw.wx.utils.ComputedGuaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yqf
 * @date 2020/11/11 下午7:39
 */
@RestController
@RequestMapping("/gua")
public class GuaController {
    @Autowired
    private GuaService guaService;
    @Autowired
    private BaseGuaService baseGuaService;
    @Autowired
    private YaoService yaoService;

    private BaseGua findGua(ComputeGua computeGua) {
        BaseGua baseGua = new BaseGua();
        Gua gua = new Gua();
        QueryWrapper<Gua> guaQueryWrapper = new QueryWrapper<>();
        //设置上卦
        gua.setOne(computeGua.getTop().get(0));
        gua.setTwo(computeGua.getTop().get(1));
        gua.setThree(computeGua.getTop().get(2));
        guaQueryWrapper.setEntity(gua);
        Gua top = guaService.getOne(guaQueryWrapper);
        baseGua.setTop(top.getId());
        //设置下卦
        gua.setOne(computeGua.getBottom().get(0));
        gua.setTwo(computeGua.getBottom().get(1));
        gua.setThree(computeGua.getBottom().get(2));
        guaQueryWrapper.setEntity(gua);
        Gua bottom = guaService.getOne(guaQueryWrapper);
        baseGua.setBottom(bottom.getId());
        //查找卦象
        QueryWrapper<BaseGua> baseGuaQueryWrapper = new QueryWrapper<>();
        baseGuaQueryWrapper.setEntity(baseGua);
        return baseGuaService.getOne(baseGuaQueryWrapper);

    }

    @GetMapping("/baseGua/{id}")
    public ResponseEntity<WxRes> getBaseGua(@PathVariable Integer id){
        BaseGua byId = baseGuaService.getById(id);
        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,byId),HttpStatus.OK);
    }

    @GetMapping("/yao/{id}")
    public ResponseEntity<WxRes> getYao(@PathVariable Integer id){
        Yao byId = yaoService.getById(id);
        return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,byId),HttpStatus.OK);
    }

    private Yao findYao(ComputeGua computeGua,Integer baseGuaId){
        Yao yao = new Yao();
        yao.setBaseGuaId(baseGuaId);
        yao.setPos(computeGua.getPos());
        QueryWrapper<Yao> yaoQueryWrapper = new QueryWrapper<>();
        yaoQueryWrapper.setEntity(yao);
        return yaoService.getOne(yaoQueryWrapper);

    }

    @PostMapping("/computed")
    public ResponseEntity<WxRes> computeGua(@RequestBody ComputeGua computeGua) {
        if (computeGua != null) {
            System.out.println(computeGua);
            ComputeGua computeGua1 = ComputedGuaUtil.computedYao(computeGua);
            System.out.println(computeGua1);
            Map<String,Object> rs = new HashMap<>(10);

            if (computeGua1.getChange()!=null && computeGua1.getPos() == null) {
                BaseGua gua = findGua(computeGua1);
                rs.put("baseGua",gua);
                rs.put("yao",null);
                System.out.println(gua);
            }

            if (computeGua1.getChange()==null && computeGua1.getPos() == null) {
                BaseGua gua = findGua(computeGua1);
                rs.put("baseGua",gua);
                rs.put("yao",null);
                System.out.println(gua);
            }

            if( computeGua1.getPos() != null) {
                BaseGua gua = findGua(computeGua1);
                System.out.println(gua);
                Yao yao = findYao(computeGua1, gua.getId());
                System.out.println(yao);
                rs.put("baseGua",gua);
                rs.put("yao",yao);

            }
            return new ResponseEntity<>(new WxRes(WxResType.SUCCESS,rs), HttpStatus.OK);

        }
        return new ResponseEntity<>(new WxRes(WxResType.FAIL), HttpStatus.OK);
    }



}

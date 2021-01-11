package com.fw.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fw.core.entity.Images;
import com.fw.core.service.BaseGuaService;
import com.fw.core.entity.BaseGua;

import com.fw.core.service.ImagesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AdminApplicationTests {

    @Autowired
    private ImagesService imagesService;
    @Test
    void contextLoads() {

        IPage<Images> page = new Page<>(1, 10);
        QueryWrapper<Images> objectQueryWrapper = new QueryWrapper<>();
        Images images = new Images();
        images.setCategoryId(3);

        objectQueryWrapper.setEntity(images);

        IPage<Images> pages = imagesService.page(page, objectQueryWrapper);

        Map<String,Object> rs = new HashMap<>(10);
        rs.put("list",pages.getRecords());
        rs.put("total",pages.getTotal());
        rs.put("size",pages.getSize());
        System.out.println(pages.getRecords());
    }

}

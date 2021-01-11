package com.fw.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.core.entity.Images;
import com.fw.core.entity.WxImages;
import com.fw.core.mapper.ImagesMapper;
import com.fw.core.mapper.WxImagesMapper;
import com.fw.core.service.ImagesService;
import com.fw.core.service.WxImagesService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
@Service
public class WxImagesServiceImpl extends ServiceImpl<WxImagesMapper, WxImages> implements WxImagesService {

}

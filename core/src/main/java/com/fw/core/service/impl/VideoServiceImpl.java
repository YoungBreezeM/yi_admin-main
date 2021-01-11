package com.fw.core.service.impl;

import com.fw.core.entity.Video;
import com.fw.core.mapper.VideoMapper;
import com.fw.core.service.VideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

}

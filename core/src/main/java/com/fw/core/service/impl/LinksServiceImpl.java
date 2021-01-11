package com.fw.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.core.entity.Links;
import com.fw.core.mapper.LinksMapper;
import com.fw.core.service.LinksService;
import org.springframework.stereotype.Service;

/**
 * @author yqf
 * @date 2020/11/28 下午8:29
 */
@Service
public class LinksServiceImpl extends ServiceImpl<LinksMapper, Links> implements LinksService {
}

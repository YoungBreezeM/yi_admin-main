package com.fw.core.service.impl;

import com.fw.core.entity.Client;
import com.fw.core.mapper.ClientMapper;
import com.fw.core.service.ClientService;
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
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements ClientService {

}

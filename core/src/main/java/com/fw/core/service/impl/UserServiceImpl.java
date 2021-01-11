package com.fw.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fw.core.entity.User;
import com.fw.core.mapper.UserMapper;
import com.fw.core.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fw.core.utils.Md5util;
import com.fw.core.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TokenUtil tokenUtil;

    @Override
    public String login(User user) {
        if(user.getPassword()!=null){
            user.setPassword(Md5util.encryption(user.getPassword()));
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>(user);
            Integer count = userMapper.selectCount(userQueryWrapper);

            if (count>0){
                return tokenUtil.sign(user);
            }
        }
        return null;
    }
}

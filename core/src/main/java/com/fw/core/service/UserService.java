package com.fw.core.service;

import com.fw.core.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
public interface UserService extends IService<User> {
    /**
     * user login
     * @param user
     * @return s
     * */
    String login(User user);
}

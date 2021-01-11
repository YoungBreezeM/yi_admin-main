package com.fw.core.service;

import com.fw.core.entity.Questions;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yqf
 * @since 2020-11-07
 */
public interface QuestionsService extends IService<Questions> {
    /**
     * add questions
     * @param questions
     * @return b
     * */
    boolean addQuestion(Questions questions);
}

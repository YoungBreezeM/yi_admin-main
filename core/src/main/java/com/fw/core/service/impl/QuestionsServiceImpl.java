package com.fw.core.service.impl;

import com.fw.core.entity.Questions;
import com.fw.core.mapper.QuestionsMapper;
import com.fw.core.service.QuestionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class QuestionsServiceImpl extends ServiceImpl<QuestionsMapper, Questions> implements QuestionsService {
    @Autowired
    private QuestionsMapper questionsMapper;
    @Override
    public boolean addQuestion(Questions questions) {
        int insert = questionsMapper.insert(questions);

        return insert>0;
    }
}

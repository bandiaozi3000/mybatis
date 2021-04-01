package com.mybatis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatis.bean.Area;
import com.mybatis.dao.AreaMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description 用户业务实现
 * @Author Sans
 * @CreateTime 2019/6/8 16:26
 */
@Service
public class AreaServiceImpl extends ServiceImpl<AreaMapper, Area> implements AreaService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void testTransaction() {
        System.out.println("测试spring事务");
    }
}
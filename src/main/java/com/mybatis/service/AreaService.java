package com.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatis.bean.Area;

/**
 * @Description 用户业务接口
 * @Author Sans
 * @CreateTime 2019/6/8 16:26
 */
public interface AreaService extends IService<Area> {

    public void testTransaction();
}
package com.mybatis.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mybatis.bean.Area;
import com.mybatis.bean.QueryObj;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AreaMapper extends BaseMapper<Area> {

    List<Area> getArea(Integer id);

    List<Area> getList(@Param("query")QueryObj queryObj);
}


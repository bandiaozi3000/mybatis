//package com.mybatis.bean;
//
//import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.reflection.MetaObject;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//@Slf4j
//public class MyDetaObjectHander implements MetaObjectHandler {
//
//    /** 默认日期时间格式 */
//    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
//
//    @Override
//    public void insertFill(MetaObject metaObject) {
//        log.info("come to insert fill .........");
//        //setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject)
//        this.setFieldValByName("createdAt",LocalDateTime.now(),metaObject);
//        this.setFieldValByName("updatedAt",LocalDateTime.now(),metaObject);
//        this.setFieldValByName("createUserId","创建人ID",metaObject);
//        this.setFieldValByName("updateUserId","更新人ID",metaObject);
//    }
//
//    @Override
//    public void updateFill(MetaObject metaObject) {
//        log.info("come to update fill .......");
//
//        this.setFieldValByName("updatedAt",LocalDateTime.now(),metaObject);
//        this.setFieldValByName("updateUserId","更新人ID",metaObject);
//        Object isDelete = this.getFieldValByName("isDelete",metaObject);
//        if(isDelete!=null&&isDelete.toString().equals("1")){
//            this.setFieldValByName("deletedAt",LocalDateTime.now(),metaObject);
//            this.setFieldValByName("deleteUserId","删除人ID",metaObject);
//        }
//
//
//    }
//}
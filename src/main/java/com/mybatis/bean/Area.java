package com.mybatis.bean;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("area")
public class Area implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
//    @TableField(fill = FieldFill.INSERT)
//    private LocalDateTime createdAt;
//    @TableField(fill = FieldFill.INSERT)
//    private String createUserId;
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updatedAt;
//    @TableField(fill = FieldFill.INSERT_UPDATE)
//    private String updateUserId;
//    @TableLogic
//    private String isDelete;
//    @TableField(fill = FieldFill.UPDATE)
//    private LocalDateTime deletedAt;
//    @TableField(fill = FieldFill.UPDATE)
//    private String deleteUserId;

}

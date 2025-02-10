package com.mxch.imgreconsturct.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_type")
public class Type {
    // id
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 类型名称
    private String typeName;
    // 类型图标
    private String iconUrl;
    // 创建时间
    private LocalDateTime createTime;
}

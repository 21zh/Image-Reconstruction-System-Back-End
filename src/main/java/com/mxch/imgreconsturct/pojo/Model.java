package com.mxch.imgreconsturct.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_model")
public class Model {
    // id
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 类型id
    private Integer typeId;
    // 图像
    private String image;
    // 模型
    private String model;
}

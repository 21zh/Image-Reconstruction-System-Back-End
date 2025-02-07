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
@TableName("tb_forum")
public class Forum {
    // id
    @TableId(value = "id",type =  IdType.AUTO)
    private Integer id;
    // 用户id
    private Integer userId;
    // 类型id
    private Integer typeId;
    // 标题
    private String title;
    // 图像
    private String image;
    // 模型
    private String model;
    // 点赞量
    private Integer likes;
    // 下载量
    private Integer downloads;
    // 创建时间
    private LocalDateTime createTime;
}

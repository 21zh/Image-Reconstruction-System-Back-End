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
@TableName("tb_reconstruct")
public class Reconstruct {
    // id
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 用户id
    private String userId;
    // 图像路径
    private String imagePath;
    // 模型路径
    private String modelPath;
    // 类型（0：手绘图像，1：摄影图像）
    private Integer type;
    // 创建时间
    private LocalDateTime createTime;
    // 修改时间
    private LocalDateTime updateTime;
}

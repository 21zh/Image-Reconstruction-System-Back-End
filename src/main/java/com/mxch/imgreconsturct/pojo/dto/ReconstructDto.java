package com.mxch.imgreconsturct.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReconstructDto {
    // 用户id
    private String userId;
    // 图片路径
    private String imagePath;
    // 模型路径
    private String modelPath;
}

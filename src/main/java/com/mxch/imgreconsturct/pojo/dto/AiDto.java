package com.mxch.imgreconsturct.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiDto implements Serializable {
    // 用户id
    private String userId;
    // 所需的关键内容
    private String prompt;
    // 不需的关键内容
    private String negativePrompt;
    // 模型选择
    private Boolean isImage;
    // base64编码的图像
    private String imagePath;
}

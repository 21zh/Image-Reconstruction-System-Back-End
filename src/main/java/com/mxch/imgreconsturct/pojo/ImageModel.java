package com.mxch.imgreconsturct.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageModel {
    // id
    private String id;
    // 图片名称
    private String imageName;
    // 图片路径
    private String imageUrl;
    // 模型路径
    private String modelUrl;
}

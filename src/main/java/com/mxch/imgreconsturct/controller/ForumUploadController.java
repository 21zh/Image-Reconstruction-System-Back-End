package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins = "*")
public class ForumUploadController {
    // 文件的存储路径
    private static final String IMAGE_DIR = "E:/server/resources/forum/image/";
    private static final String MODEL_DIR = "E:/server/resources/forum/model/";

    /**
     * 图片上传处理
     * @param file
     * @return
     */
    @PostMapping("/imageUpload")
    public Result imageUpload(@RequestParam("file") MultipartFile file){
        // 文件为空
        if (file.isEmpty()){
            return Result.build(null, ResultCodeEnum.NO_IMAGE);
        }

        try {
            // 判断图像存储路径是否存在
            File avatarDir = new File(IMAGE_DIR);
            if (!avatarDir.exists()){
                avatarDir.mkdirs();
            }

            // 生成唯一文件名
            String imageName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String imagePath = IMAGE_DIR + imageName;

            // 保存文件
            file.transferTo(new File(imagePath));

            // 返回文件访问路径
            return  Result.ok("http://localhost:1800/forum/image/" + imageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 模型上传处理
     */
    @PostMapping("/modelUpload")
    public Result modelUpload(@RequestParam("file") MultipartFile file){
        // 文件为空
        if (file.isEmpty()){
            return Result.build(null, ResultCodeEnum.NO_MODEL);
        }

        try {
            // 判断图像存储路径是否存在
            File avatarDir = new File(MODEL_DIR);
            if (!avatarDir.exists()){
                avatarDir.mkdirs();
            }

            // 生成唯一文件名
            String modelName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String modelPath = MODEL_DIR + modelName;

            // 保存文件
            file.transferTo(new File(modelPath));

            // 返回文件访问路径
            return  Result.ok("http://localhost:1800/forum/model/" + modelName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

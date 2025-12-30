package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.util.Aliyunoss;
import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.ResultCodeEnum;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.mxch.imgreconsturct.util.OssConstants.*;

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
//            File avatarDir = new File(IMAGE_DIR);
//            if (!avatarDir.exists()){
//                avatarDir.mkdirs();
//            }

            // 生成唯一文件名
            String imageName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String objectName = IMAGE_FILES + imageName;

            // 保存文件
//            file.transferTo(new File(imagePath));

            // 转化为文件流
            InputStream inputStream = file.getInputStream();
            String imagePath = Aliyunoss.uploadAliyunOssByFiles(inputStream, FORUM_BUCKET_NAME, objectName);

            // 返回文件访问路径
            return  Result.ok(imagePath);
        } catch (Exception e) {
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
//            File avatarDir = new File(MODEL_DIR);
//            if (!avatarDir.exists()){
//                avatarDir.mkdirs();
//            }

            // 生成唯一文件名
            String modelName = UUID.randomUUID() + "_" + file.getOriginalFilename();
//            String modelPath = MODEL_DIR + modelName;
            String objectName = MODEL_FILES + modelName;

            // 保存文件
//            file.transferTo(new File(modelPath));

            // 转化为文件流
            InputStream inputStream = file.getInputStream();
            String modelPath = Aliyunoss.uploadAliyunOssByFiles(inputStream, FORUM_BUCKET_NAME, objectName);

            // 返回文件访问路径
            return  Result.ok(modelPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

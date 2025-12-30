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

import static com.mxch.imgreconsturct.util.OssConstants.AVATAR_FILES;
import static com.mxch.imgreconsturct.util.OssConstants.DEFAULT_BUCKET_NAME;

@RestController
@RequestMapping("/avatar")
@CrossOrigin(origins = "*")
public class AvatarUploadController {
    // 文件的存储路径
//    private static final String AVATAR_DIR = "E:/server/resources/avatar/";

    @PostMapping("/upload")
    public Result avatarUpload(@RequestParam("file") MultipartFile file) {
        // 文件为空
        if (file.isEmpty()){
            return Result.build(null, ResultCodeEnum.NO_AVATAR);
        }

        String avatarName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String objectName = AVATAR_FILES + avatarName;

        try {
            // 转化为文件流
            InputStream inputStream = file.getInputStream();
            String avatarPath = Aliyunoss.uploadAliyunOssByFiles(inputStream, DEFAULT_BUCKET_NAME, objectName);

            // 判断头像存储路径是否存在
            /*File avatarDir = new File(AVATAR_DIR);
            if (!avatarDir.exists()){
                avatarDir.mkdirs();
            }*/
            // 生成唯一文件名
//            String avatarPath = AVATAR_DIR + avatarName;
            // 保存文件
//            file.transferTo(new File(avatarPath));

            // 返回文件访问路径
            return  Result.ok(avatarPath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

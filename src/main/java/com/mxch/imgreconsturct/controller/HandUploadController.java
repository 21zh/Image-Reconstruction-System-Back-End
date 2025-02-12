package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.util.ImageReconstruct;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/handDraw")
@CrossOrigin(origins = "*")
public class HandUploadController {

    // 文件的存储路径
    private static final String IMAGE_DIR = "E:/server/resources/handDraw/image/";
    private static final String MODEL_DIR = "E:/server/resources/handDraw/model/";

    @PostMapping("/imageUpload")
    public Result handReconstruct(@RequestBody Map<String,String> imageData) throws IOException {
        try{
            // 解析图片数据
            String base64Data = imageData.get("image").split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            UUID randomStr = UUID.randomUUID();

            String imageName = randomStr + ".png";
            String imagePath = IMAGE_DIR + imageName;

            String modelName = randomStr + ".binvox";
            String modelPath = MODEL_DIR + modelName;

            // 将文件写入到文件夹中
            Files.write(Paths.get(imagePath),imageBytes);

            // 调用python脚本进行重构模型
            ImageReconstruct.reconstructImage(imagePath,modelPath);

            return Result.ok("http://localhost:1800/handDraw/model/" + modelName);
        }catch (Exception e){
            return Result.fail();
        }
    }
}

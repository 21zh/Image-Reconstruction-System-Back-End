package com.mxch.imgreconsturct.controller;

import cn.hutool.json.JSONUtil;
import com.google.gson.JsonObject;
import com.mxch.imgreconsturct.pojo.dto.ReconstructDto;
import com.mxch.imgreconsturct.service.ReconstructService;
import com.mxch.imgreconsturct.util.Aliyunoss;
import com.mxch.imgreconsturct.util.ImageReconstruct;
import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Resource
    private ImageReconstruct imageReconstruct;

    @Resource
    private ReconstructService reconstructService;

    @PostMapping("/handUpload")
    public Result handUpload(@RequestBody Map<String,String> imageData) throws IOException {
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

    /**
     * 手绘三维重建
     * @param imageData 参数
     * @return
     */
    @PostMapping("/handReconstruct")
    public Result handReconstruct(@RequestBody Map<String, String> imageData) {
        // 文件名称
        UUID randomStr = UUID.randomUUID();
        String imageName = randomStr + ".png";
        String modelName = randomStr + ".binvox";

        try {
            // 解析图片数据
            String base64Data = imageData.get("image").split(",")[1];
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);
            InputStream inputStream = new ByteArrayInputStream(imageBytes);

            // 上传图像文件到oss
            Map<String, String> paths = Aliyunoss.uploadAliyunOssByHand(inputStream, imageName, modelName);
            if (paths.isEmpty()) {
                return Result.fail("文件上传失败");
            }

            // 三维重建图像
            String userId = UserThreadLocal.getUserId();
            imageReconstruct.reconstructByHandOrImage(false, paths.get("imagePath"), paths.get("modelPath"), userId);

            return Result.ok("任务已提交");
        } catch (Exception e) {
            return Result.fail("任务出错，请重试");
        }
    }

    /**
     * 方法回调获取手绘三维重建结果
     * @param reconstructDto 三维重建结果
     * @return
     */
    @PostMapping("/handReconstructNotice")
    public Result handReconstructNotice(@RequestBody ReconstructDto reconstructDto) {
        try {
            // 发送给对应用户
            imageReconstruct.handSendToUser(reconstructDto);

            // 存储与数据库中
            reconstructService.addReconstruct(false, reconstructDto);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail();
        }
    }


}

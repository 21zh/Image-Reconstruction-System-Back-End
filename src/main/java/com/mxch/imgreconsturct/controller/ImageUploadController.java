package com.mxch.imgreconsturct.controller;

import cn.hutool.core.util.RandomUtil;
import com.mxch.imgreconsturct.pojo.ImageModel;
import com.mxch.imgreconsturct.pojo.dto.ReconstructDto;
import com.mxch.imgreconsturct.util.ImageReconstruct;
import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/imageDraw")
@CrossOrigin(origins = "*")
public class ImageUploadController {

    @Resource
    private ImageReconstruct imageReconstruct;

    // 文件的存储路径
    private static final String IMAGE_DIR = "E:/server/resources/imageDraw/image/";
    private static final String MODEL_DIR = "E:/server/resources/imageDraw/model/";

    /**
     * 处理单个或者多个图片的逻辑
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result imageReconstruct(@RequestParam("file") MultipartFile file) throws IOException {
        // 检查文件是否为空
        if (file == null) {
            return Result.build(null, ResultCodeEnum.NO_FILE);
        }
        try{
            UUID randomStr = UUID.randomUUID();

            String imageName = randomStr + ".png";
            String imagePath = IMAGE_DIR + imageName;

            String modelName = randomStr + ".binvox";
            String modelPath = MODEL_DIR + modelName;

            // 将文件写入到文件夹中
            file.transferTo(new File(imagePath));

            // 调用python脚本进行重构模型
            ImageReconstruct.reconstructImage(imagePath,modelPath);

            // 创建对象存储数据
            ImageModel imageModel = new ImageModel();
            imageModel.setId(UUID.randomUUID().toString());
            imageModel.setImageName(file.getOriginalFilename());
            imageModel.setImageUrl("http://localhost:1800/imageDraw/image/" + imageName);
            imageModel.setModelUrl("http://localhost:1800/imageDraw/model/" + modelName);

            // 放回结果
            return Result.ok(imageModel);

        }catch (Exception e){
            return Result.fail();
        }
    }

    /**
     * 处理文件夹的逻辑
     * @param request
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @PostMapping("/fileUpload")
    public Result fileReconstruct(MultipartHttpServletRequest request) throws IOException, InterruptedException {
        // 放回的数据结果
        List<ImageModel> imageModels = new ArrayList<>();
        // 判断文件夹是否为空
        if(request.getFiles("files").isEmpty()){
            return Result.fail();
        }
        for (MultipartFile file : request.getFiles("files")) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            // 文件类型必须为图片类型
            if(fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")){
                String[] splits = fileName.split("/");
                String imageName = splits[splits.length - 1];
                String imagePath = IMAGE_DIR + imageName;
                String modelName = imageName.split("\\.")[0] + ".binvox";
                String modelPath = MODEL_DIR + modelName;

                // 将文件写入到文件夹中
                file.transferTo(new File(imagePath));

                // 调用python脚本进行重构模型
                ImageReconstruct.reconstructImage(imagePath,modelPath);

                // 创建对象存储数据
                ImageModel imageModel = new ImageModel();
                imageModel.setId(UUID.randomUUID().toString());
                imageModel.setImageName(imageName);
                imageModel.setImageUrl("http://localhost:1800/imageDraw/image/" + imageName);
                imageModel.setModelUrl("http://localhost:1800/imageDraw/model/" + modelName);

                imageModels.add(imageModel);
            }
        }
        return Result.ok(imageModels);
    }

    /**
     * 方法回调获取图像三维重建结果
     * @param reconstructDto    三维重建结果
     * @return
     */
    @PostMapping("/imageReconstructNotice")
    public Result imageReconstructNotice(@RequestBody ReconstructDto reconstructDto) {
        try {
            // 发送给对应用户
            imageReconstruct.handSendToUser(reconstructDto);

            return Result.ok();
        } catch (Exception e) {
            return Result.fail();
        }
    }
}

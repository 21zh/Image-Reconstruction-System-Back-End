package com.mxch.imgreconsturct.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxch.imgreconsturct.pojo.Room;
import com.mxch.imgreconsturct.pojo.RoomUser;
import com.mxch.imgreconsturct.pojo.dto.AiDto;
import com.mxch.imgreconsturct.pojo.dto.ReconstructDto;
import com.mxch.imgreconsturct.pojo.dto.RoomMsg;
import com.mxch.imgreconsturct.service.RoomService;
import com.mxch.imgreconsturct.service.RoomUserService;
import com.mxch.imgreconsturct.util.Aliyunoss;
import com.mxch.imgreconsturct.util.ImageReconstruct;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.mxch.imgreconsturct.util.OssConstants.*;

@Component
public class ConsumerServiceImpl {

    // AI模型平台api
    private static final String BEARER_KEY = System.getenv("BEARER_KEY");

    // AI协作地址
    private static final String AI_HELP_URL = "https://api.siliconflow.cn/v1/images/generations";
    @Resource
    private RoomService roomService;

    @Resource
    private RoomUserService roomUserService;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private ImageReconstruct imageReconstruct;

    @RabbitListener(
            queues = "room.queue",
            concurrency = "5-6"
    )
    public void roomReceive(RoomMsg roomMsg) {
        // 获取房间消息
        Room room = roomService.getById(roomMsg.getRoomId());

        // 更新房间消息
        int count = room.getUserNum() + 1;
        room.setUserNum(count);
        room.setUpdateTime(LocalDateTime.now());
        roomService.updateById(room);

        // 添加房间和用户的关联关系
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(String.valueOf(room.getId()));
        roomUser.setUserId(roomMsg.getUserId());
        roomUser.setJoinTime(LocalDateTime.now());
        roomUserService.save(roomUser);
    }

    @RabbitListener(
            queues = "ai.queue",
            concurrency = "5-6"
    )
    public void aiReceive(AiDto aiDto) throws Exception {
        // 构造请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", BEARER_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构造请求体
        Map<String, Object> payload = new HashMap<>();
        if (aiDto.getIsImage()) {
            payload.put("model", "Qwen/Qwen-Image-Edit");
            // 解析图像数据
            byte[] imageBytes = Base64.getDecoder().decode(aiDto.getImagePath().split(",")[1]);

            // 上传到oss
            InputStream inputStream = new ByteArrayInputStream(imageBytes);
            String imagePath = Aliyunoss.uploadAliyunOssByFiles(inputStream, DEFAULT_BUCKET_NAME, AI_SOURCE_FILES);

            payload.put("image", imagePath);
        } else {
            payload.put("model", "Qwen/Qwen-Image");
        }

        payload.put("prompt", aiDto.getPrompt());
        payload.put("negative_prompt", aiDto.getNegativePrompt());
        payload.put("image_size", "1328x1328");
        payload.put("batch_size", 1);
        payload.put("seed", ThreadLocalRandom.current().nextLong(0, 10000000000L));
        payload.put("num_inference_steps", 20);
        payload.put("guidance_scale", 7.5);
        payload.put("cfg", 4);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        String res = restTemplate.postForObject(AI_HELP_URL, entity, String.class);
        // 结果解析
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(res);
        String imagePath = root
                .path("images")
                .get(0)
                .path("url")
                .asText();

        // 下载图像到oss中
        InputStream outputStream = new URL(imagePath).openStream();
        String targetPath = Aliyunoss.uploadAliyunOssByFiles(outputStream, DEFAULT_BUCKET_NAME, AI_TARGET_FILES);

        // 结果推送
        ReconstructDto reconstructDto = new ReconstructDto();
        reconstructDto.setUserId(aiDto.getUserId());
        reconstructDto.setImagePath(targetPath);

        imageReconstruct.aiSendToUser(reconstructDto);
    }

}

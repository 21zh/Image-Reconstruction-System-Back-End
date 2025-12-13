package com.mxch.imgreconsturct.util;

import com.mxch.imgreconsturct.pojo.dto.ReconstructDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class ImageReconstruct {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private SimpMessagingTemplate messagingTemplate;

    // 手绘重建的url地址
    private static final String HAND_RECONSTRUCT_URL = "http://localhost:8000/handReconstruct";
    // 摄影重建的url地址
    private static final String IMAGE_RECONSTRUCT_URL = "http://localhost:8000/imageReconstruct";

    public static void reconstructImage(String imagePath, String modelPath) throws IOException, InterruptedException {
        try {
            // python脚本路径
            String pythonScriptPath = "E:\\机电-计科\\计算机科学与技术\\毕业设计\\ImageReconstruction\\serverProject\\imgReconsturct\\src\\main\\resources\\python\\runner.py";

            // 传递图片和模型路径参数
            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, imagePath, modelPath);
            Process process = processBuilder.start();

            // 等待脚本执行
            int exitCode = process.waitFor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 发送三维重建请求
     * @param type  类型
     * @param imageName 图像名称
     * @param imagePath 图像路径
     * @param modelPath 模型路径
     * @param userId    用户id
     * @return  数据
     */
    public String reconstructByHandOrImage(boolean type, String imageName, String imagePath, String modelPath, String userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构造请求参数
        Map<String, String> payload = new HashMap<>();
        payload.put("imageName", imageName);
        payload.put("imagePath", imagePath);
        payload.put("modelPath", modelPath);
        payload.put("userId", userId);

        // 构造请求体
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);

        return restTemplate.postForObject(type ? IMAGE_RECONSTRUCT_URL : HAND_RECONSTRUCT_URL, entity, String.class);
    }

    /**
     * 推送手绘重建结果
     * @param reconstructDto    重建结果
     */
    public void handSendToUser(ReconstructDto reconstructDto) {
        String userId = reconstructDto.getUserId();

        messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/reconstruct/handNotice",
                reconstructDto
        );
    }

    /**
     * 推送图像重建结果
     * @param reconstructDto    重建结果
     */
    public void imageSendToUser(List<ReconstructDto> reconstructDto) {
        String userId = reconstructDto.get(0).getUserId();

        messagingTemplate.convertAndSendToUser(
                userId,
                "/queue/reconstruct/imageNotice",
                reconstructDto
        );
    }
}

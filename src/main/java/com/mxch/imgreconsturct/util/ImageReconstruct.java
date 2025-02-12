package com.mxch.imgreconsturct.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Slf4j
public class ImageReconstruct {
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
}

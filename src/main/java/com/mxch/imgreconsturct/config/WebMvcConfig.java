package com.mxch.imgreconsturct.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置头像资源的地址
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:E:/server/resources/avatar/");

        // 配置类型图标的地址
        registry.addResourceHandler("/icon/**")
                .addResourceLocations("file:E:/server/resources/icon/");

        // 配置图像地址
        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:E:/server/resources/image/");

        // 配置模型地址
        registry.addResourceHandler("/model/**")
                .addResourceLocations("file:E:/server/resources/model/");

        // 配置手绘重构的图像数据地址
        registry.addResourceHandler("/handDraw/image/**")
                .addResourceLocations("file:E:/server/resources/handDraw/image/");

        // 配置手绘重构的模型数据地址
        registry.addResourceHandler("/handDraw/model/**")
                .addResourceLocations("file:E:/server/resources/handDraw/model/");

        // 配置图像重构的图像数据地址
        registry.addResourceHandler("/imageDraw/image/**")
                .addResourceLocations("file:E:/server/resources/imageDraw/image/");

        // 配置图像重构的模型数据地址
        registry.addResourceHandler("/imageDraw/model/**")
                .addResourceLocations("file:E:/server/resources/imageDraw/model/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 配置跨域请求
        registry.addMapping("/**")  // 允许跨域的路径（这里是所有路径）
                .allowedOrigins("http://localhost:3000")  // 允许来自 http://localhost:3000 的请求
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的 HTTP 方法
                .allowedHeaders("*")  // 允许所有请求头
                .allowCredentials(true);  // 允许携带认证信息
    }
}

package com.mxch.imgreconsturct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.mxch.imgreconsturct.mapper")
@SpringBootApplication
public class ImgReconsturctApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImgReconsturctApplication.class, args);
    }

}

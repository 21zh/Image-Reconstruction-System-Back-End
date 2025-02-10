package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.service.TypeService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/type")
public class TypeController {

    @Resource
    private TypeService typeService;

    /**
     * 获取所有的类型
     * @return
     */
    @GetMapping("/getAllType")
    public Result getAllType() {
        return typeService.getAllType();
    }
}

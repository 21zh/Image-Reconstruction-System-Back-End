package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.service.ModelService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/model")
public class ModelController {

    @Resource
    private ModelService modelService;

    /**
     * 获取对应类型下的模型和图片数据
     * @param typeId
     * @return
     */
    @GetMapping("/getTypeModel/{typeId}")
    public Result getTypeModel(@PathVariable("typeId") Integer typeId){
        return modelService.getTypeModel(typeId);
    }
}

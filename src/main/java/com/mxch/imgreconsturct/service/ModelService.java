package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.Model;
import com.mxch.imgreconsturct.util.Result;

public interface ModelService extends IService<Model> {
    public Result getTypeModel(Integer typeId);
}

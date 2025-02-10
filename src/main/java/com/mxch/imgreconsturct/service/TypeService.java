package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.Type;
import com.mxch.imgreconsturct.util.Result;

public interface TypeService extends IService<Type> {
    public Result getAllType();
}

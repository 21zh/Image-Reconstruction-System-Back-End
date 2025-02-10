package com.mxch.imgreconsturct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.ModelMapper;
import com.mxch.imgreconsturct.pojo.Model;
import com.mxch.imgreconsturct.service.ModelService;
import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements ModelService {
    /**
     * 获取类型数据的逻辑处理
     *
     * @param typeId
     * @return
     */
    @Override
    public Result getTypeModel(Integer typeId) {
        // 如果id为0，则查询全部数据
        if (typeId == 0) {
            List<Model> modelList = list();
            return Result.ok(modelList);
        }
        //否则，根据id查询对应数据
        List<Model> typeModelList = query().eq("type_id", typeId).list();
        return Result.ok(typeModelList);
    }
}

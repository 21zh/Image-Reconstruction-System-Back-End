package com.mxch.imgreconsturct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.TypeMapper;
import com.mxch.imgreconsturct.pojo.Type;
import com.mxch.imgreconsturct.service.TypeService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    /**
     * 获取所有类型的逻辑处理
     * @return
     */
    @Override
    public Result getAllType() {
        List<Type> typeList = list();
        return Result.ok(typeList);
    }
}

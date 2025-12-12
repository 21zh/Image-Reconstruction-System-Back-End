package com.mxch.imgreconsturct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.ReconstructMapper;
import com.mxch.imgreconsturct.pojo.Reconstruct;
import com.mxch.imgreconsturct.pojo.dto.ReconstructDto;
import com.mxch.imgreconsturct.service.ReconstructService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
public class ReconstructServiceImpl extends ServiceImpl<ReconstructMapper, Reconstruct> implements ReconstructService {
    /**
     * 添加三维重建的结果
     * @param reconstructDto    重建结果
     */
    @Override
    public void addReconstruct(boolean flag, ReconstructDto reconstructDto) {
        // 存储结果实体
        Reconstruct reconstruct = new Reconstruct();

        // 拷贝字段数据
        BeanUtils.copyProperties(reconstructDto, reconstruct);
        reconstruct.setType(flag ? 1 : 0);
        reconstruct.setCreateTime(LocalDateTime.now());
        reconstruct.setUpdateTime(LocalDateTime.now());

        save(reconstruct);
    }
}

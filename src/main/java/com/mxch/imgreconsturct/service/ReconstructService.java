package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.Reconstruct;
import com.mxch.imgreconsturct.pojo.dto.ReconstructDto;

public interface ReconstructService extends IService<Reconstruct> {
    void addReconstruct(boolean flag, ReconstructDto reconstructDto);
}

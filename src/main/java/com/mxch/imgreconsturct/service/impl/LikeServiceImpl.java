package com.mxch.imgreconsturct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.LikeMapper;
import com.mxch.imgreconsturct.pojo.Likes;
import com.mxch.imgreconsturct.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class LikeServiceImpl extends ServiceImpl<LikeMapper, Likes> implements LikeService {

}

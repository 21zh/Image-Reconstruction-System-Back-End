package com.mxch.imgreconsturct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.RoomUserMapper;
import com.mxch.imgreconsturct.pojo.RoomUser;
import com.mxch.imgreconsturct.service.RoomUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RoomUserServiceImpl extends ServiceImpl<RoomUserMapper, RoomUser> implements RoomUserService {
}

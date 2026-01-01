package com.mxch.imgreconsturct.service.impl;

import com.mxch.imgreconsturct.pojo.Room;
import com.mxch.imgreconsturct.pojo.RoomUser;
import com.mxch.imgreconsturct.pojo.dto.RoomMsg;
import com.mxch.imgreconsturct.service.RoomService;
import com.mxch.imgreconsturct.service.RoomUserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Component
public class ConsumerServiceImpl {

    @Resource
    private RoomService roomService;

    @Resource
    private RoomUserService roomUserService;

    @RabbitListener(
            queues = "room.queue",
            concurrency = "5-6"
    )
    public void receive(RoomMsg roomMsg) {
        // 获取房间消息
        Room room = roomService.getById(roomMsg.getRoomId());

        // 更新房间消息
        int count = room.getUserNum() + 1;
        room.setUserNum(count);
        room.setUpdateTime(LocalDateTime.now());
        roomService.updateById(room);

        // 添加房间和用户的关联关系
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(String.valueOf(room.getId()));
        roomUser.setUserId(roomMsg.getUserId());
        roomUser.setJoinTime(LocalDateTime.now());
        roomUserService.save(roomUser);
    }
}

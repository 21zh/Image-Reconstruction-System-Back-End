package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.Room;
import com.mxch.imgreconsturct.pojo.RoomUser;
import com.mxch.imgreconsturct.pojo.Stroke;
import com.mxch.imgreconsturct.pojo.dto.RoomDto;
import com.mxch.imgreconsturct.pojo.dto.StrokeDto;
import com.mxch.imgreconsturct.util.Result;

import java.util.List;

public interface RoomService extends IService<Room> {
    int createRoom(Room room);

    void leaveRoom(RoomUser roomUser);

    List<RoomDto> getRoomList(String roomName);

    Result enterRoom(Room room);

    RoomDto getRoomInfo(String userId);

    void uploadOperation(StrokeDto stroke);

    List<Stroke> getOperation(String userId, String roomId);
}

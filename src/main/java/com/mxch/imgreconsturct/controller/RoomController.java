package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.pojo.Room;
import com.mxch.imgreconsturct.pojo.RoomUser;
import com.mxch.imgreconsturct.pojo.Stroke;
import com.mxch.imgreconsturct.pojo.dto.RoomDto;
import com.mxch.imgreconsturct.pojo.dto.StrokeDto;
import com.mxch.imgreconsturct.service.RoomService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    private RoomService roomService;

    /**
     * 创建房间
     * @param room 房间
     * @return
     */
    @PostMapping("/createRoom")
    public Result createRoom(@RequestBody Room room) {
        try {
            int roomId = roomService.createRoom(room);
            return Result.ok(roomId);
        } catch (Exception e) {
            return Result.fail("房间创建失败");
        }
    }

    /**
     * 获取用户加入房间的信息
     * @param userId
     * @return
     */
    @GetMapping("/getRoomInfo")
    public Result getRoomInfo(@RequestParam("userId") String userId) {
        try {
            RoomDto roomDto = roomService.getRoomInfo(userId);
            return Result.ok(roomDto);
        } catch (Exception e) {
            return Result.fail("房间获取失败");
        }
    }

    /**
     * 获取房间列表
     * @return
     */
    @GetMapping("/getRoomList")
    public Result getRoomList(@RequestParam("roomName") String roomName) {
        try {
            List<RoomDto> roomList = roomService.getRoomList(roomName);
            return Result.ok(roomList);
        } catch (Exception e) {
            return Result.fail("用户列表获取失败");
        }

    }

    /**
     * 加入房间
     * @param room 房间信息
     * @return
     */
    @PostMapping("/enterRoom")
    public Result enterRoom(@RequestBody Room room) {
        try {
            return roomService.enterRoom(room);
        } catch (Exception e) {
            return Result.fail("加入房间出现问题");
        }

    }

    /**
     * 退出房间
     * @param roomUser  房间和用户
     * @return
     */
    @PostMapping("/leaveRoom")
    public Result leaveRoom(@RequestBody RoomUser roomUser) {
        try {
            roomService.leaveRoom(roomUser);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail("退出房间失败");
        }
    }

    @PostMapping("/uploadOperation")
    public Result uploadOperation(@RequestBody StrokeDto strokeDto) {
        try {
            roomService.uploadOperation(strokeDto);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail("记录上传失败");
        }
    }

    @GetMapping("/getOperation")
    public Result getOperation(@RequestParam("userId") String userId, @RequestParam("roomId") String roomId) {
        try {
            List<Stroke> strokeList = roomService.getOperation(userId, roomId);
            return Result.ok(strokeList);
        } catch (Exception e) {
            return Result.fail("房间记录获取失败");
        }
    }
}

package com.mxch.imgreconsturct.service.impl;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.RoomMapper;
import com.mxch.imgreconsturct.mapper.RoomUserMapper;
import com.mxch.imgreconsturct.mapper.StrokeMapper;
import com.mxch.imgreconsturct.pojo.Room;
import com.mxch.imgreconsturct.pojo.RoomUser;
import com.mxch.imgreconsturct.pojo.Stroke;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.pojo.dto.RoomDto;
import com.mxch.imgreconsturct.pojo.dto.RoomMsg;
import com.mxch.imgreconsturct.pojo.dto.StrokeDto;
import com.mxch.imgreconsturct.service.RoomService;
import com.mxch.imgreconsturct.service.RoomUserService;
import com.mxch.imgreconsturct.service.StrokeService;
import com.mxch.imgreconsturct.service.UserService;
import com.mxch.imgreconsturct.util.MD5;
import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mxch.imgreconsturct.util.RedisConstants.ROOM_COUNT_KEY;

@Service
@Slf4j
@Transactional
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Resource
    private RoomUserService roomUserService;

    @Resource
    private UserService userService;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private RoomUserMapper roomUserMapper;

    @Resource
    private StrokeService strokeService;

    @Resource
    private StrokeMapper strokeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private LuaRedisServiceImpl luaRedisService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 创建房间
     * @param room  房间信息
     * @return
     */
    @Override
    public int createRoom(Room room) {
        // 房间是否公开
        if (!room.getIsPublic()) {
            String passWord = MD5.encrypt(room.getRoomPassword());
            room.setRoomPassword(passWord);
        }


        // 创建房间实体
        room.setUserId(UserThreadLocal.getUserId());
        room.setUserNum(1);
        room.setCreateTime(LocalDateTime.now());
        room.setUpdateTime(LocalDateTime.now());

        save(room);

        QueryWrapper<Room> qw = new QueryWrapper<>();
        qw.eq("user_id", UserThreadLocal.getUserId());
        qw.orderByDesc("create_time");
        qw.last("Limit 1");
        Room newRoom = getOne(qw);

        // 创建房间-用户实体
        RoomUser roomUser = new RoomUser();
        roomUser.setRoomId(String.valueOf(newRoom.getId()));
        roomUser.setUserId(newRoom.getUserId());
        roomUser.setJoinTime(newRoom.getCreateTime());

        String roomKey = ROOM_COUNT_KEY + newRoom.getId();
        stringRedisTemplate.opsForValue().set(roomKey, String.valueOf(newRoom.getMaxUser() - newRoom.getUserNum()));
        roomUserService.save(roomUser);

        return newRoom.getId();
    }

    /**
     * 离开房间
     * @param roomUser  用户——房间信息
     */
    @Override
    public void leaveRoom(RoomUser roomUser) {
        QueryWrapper<RoomUser> qw = new QueryWrapper<>();
        // 获取房间和用户id
        String userId = roomUser.getUserId();
        String roomId = roomUser.getRoomId();

        String roomKey = ROOM_COUNT_KEY + roomId;

        // 查询相关房间信息
        Room room = getById(roomId);
        // 房主退出
        if (room.getUserId().equals(userId)) {
            room.setIsExist(false);
            room.setUserNum(0);
            updateById(room);
            qw.eq("room_id", roomId);
            roomUserService.remove(qw);

            // 删除redis中的房间消息
            Boolean isExist = stringRedisTemplate.hasKey(roomKey);
            if (Boolean.TRUE.equals(isExist)) {
                stringRedisTemplate.delete(roomKey);
            }
            return;
        }

        // 非房主退出
        room.setUserNum(room.getUserNum() - 1);
        updateById(room);
        qw.eq("user_id", userId);
        roomUserService.remove(qw);

        // 更新redis中的人数
        if (Boolean.TRUE.equals(stringRedisTemplate.hasKey(roomKey))) {
            stringRedisTemplate.opsForValue().set(roomKey, String.valueOf(Integer.parseInt(stringRedisTemplate.opsForValue().get(roomKey)) + 1));
        }
    }

    /**
     * 获取房间列表
     * @return
     */
    @Override
    public List<RoomDto> getRoomList(String roomName) {
        // 房间必须是存在的
        QueryWrapper<Room> qw = new QueryWrapper<>();
        qw.eq("is_exist", true);
        if (StringUtils.isNotBlank(roomName)) {
            qw.like("room_name", roomName);
        }

        List<Room> rooms = roomMapper.selectList(qw);

        List<RoomDto> roomdtoList = rooms.stream().map(
                room -> {
                    RoomDto roomDto = new RoomDto();
                    String roomId = String.valueOf(room.getId());

                    // 获取用户的头像列表
                    QueryWrapper<RoomUser> qw1 = new QueryWrapper<>();
                    qw1.eq("room_id", roomId);
                    List<RoomUser> roomUsers = roomUserMapper.selectList(qw1);
                    List<String> avatars = roomUsers.stream().map(
                            roomUser -> {
                                User user = userService.getById(roomUser.getUserId());
                                return user.getAvatar();
                            }
                    ).collect(Collectors.toList());

                    // 封装返回数据的类型
                    roomDto.setRoomId(roomId);
                    roomDto.setRoomName(room.getRoomName());
                    roomDto.setIsPublic(room.getIsPublic());
                    roomDto.setUsers(avatars);
                    roomDto.setMaxUser(room.getMaxUser());

                    return roomDto;
                }
        ).collect(Collectors.toList());


        return roomdtoList;
    }

    /**
     * 加入房间
     * @param room  房间信息
     * @return
     */
    @Override
    public Result enterRoom(Room room) {
        Room orignalRoom = getById(room.getId());
        if (!room.getIsPublic()) {
            String roomPassword = room.getRoomPassword();
            String encryptPassword = MD5.encrypt(roomPassword);
            if (!orignalRoom.getRoomPassword().equals(encryptPassword)) {
                return Result.fail("房间密码错误");
            }
        }

        if (UserThreadLocal.getUserId().equals(room.getUserId())) {
            return Result.fail("已在房间中");
        }

        String queueName = "room.queue";
        Long res = luaRedisService.execute(UserThreadLocal.getUserId(), String.valueOf(room.getId()));

        switch (res.intValue()) {
            case 1:
                // 发送消息给消息队列
                rabbitTemplate.convertAndSend(queueName, new RoomMsg(room.getId(), UserThreadLocal.getUserId()));
                break;
            case 0:
                throw new RuntimeException("房间人数已满");
            case -2:
                throw new RuntimeException("用户重复加入");
            default:
                throw new RuntimeException("系统出现异常");
        }

        return Result.ok("成功加入房间");
    }

    /**
     * 获取用户所在房间的信息
     * @param userId    用户id
     * @return
     */
    @Override
    public RoomDto getRoomInfo(String userId) {
        // 查询用户所在房间
        QueryWrapper<RoomUser> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        RoomUser roomUser = roomUserService.getOne(qw);

        if (roomUser == null) {
            return null;
        }

        // 查询返回的结果
        RoomDto roomDto = new RoomDto();
        Room room = getById(roomUser.getRoomId());

        // 拷贝实体字段
        BeanUtils.copyProperties(room, roomDto);
        roomDto.setIsOwner(room.getUserId().equals(userId));
        roomDto.setRoomId(String.valueOf(room.getId()));

        QueryWrapper<RoomUser> qw1 = new QueryWrapper<>();
        qw1.eq("room_id", room.getId());
        List<RoomUser> roomUserList = roomUserMapper.selectList(qw1);

        // 获取用户头像列表
        List<String> avatarList = roomUserList.stream().map(
                ru -> {
                    String uid = ru.getUserId();
                    User user = userService.getById(uid);
                    return user.getAvatar();
                }
        ).collect(Collectors.toList());

        roomDto.setUsers(avatarList);
        return roomDto;
    }

    /**
     * 上传用户在房间的操作
     * @param strokeDto 操作信息
     */
    @Override
    public void uploadOperation(StrokeDto strokeDto) {
        Stroke stroke = new Stroke();
        // 字段赋值
        BeanUtils.copyProperties(strokeDto, stroke, "points");
        // 转化为字符串类型
        if (!strokeDto.getType().equals("clear")) {
            stroke.setPoints(
                    JSON.toJSONString(strokeDto.getPoints())
            );
        }
        stroke.setOpearteTime(LocalDateTime.now());

        strokeService.save(stroke);
    }

    /**
     * 获取所有用户在房间的操作
     * @param userId    用户id
     * @param roomId    房间id
     * @return
     */
    @Override
    public List<Stroke> getOperation(String userId, String roomId) {
        // 查询用户是否在房间中
        QueryWrapper<RoomUser> roomUserQueryWrapper = new QueryWrapper<>();
        roomUserQueryWrapper.eq("user_id", userId);
        roomUserQueryWrapper.eq("room_id", roomId);
        int count = roomUserMapper.selectCount(roomUserQueryWrapper);
        if (count == 0) {
            return null;
        }

        // 查询操作记录
        QueryWrapper<Stroke> strokeQueryWrapper = new QueryWrapper<>();
        strokeQueryWrapper.eq("room_id", roomId);
        strokeQueryWrapper.orderByAsc("opearte_time");
        return strokeMapper.selectList(strokeQueryWrapper);
    }
}

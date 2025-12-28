package com.mxch.imgreconsturct.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomUser {
    // id
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 用户id
    private String userId;
    // 房间id
    private String roomId;
    // 加入房间的时间
    private LocalDateTime joinTime;
}

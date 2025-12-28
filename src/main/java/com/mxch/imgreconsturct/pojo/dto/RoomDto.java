package com.mxch.imgreconsturct.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDto {
    // 房间编号
    private String roomId;
    // 房间名称
    private String roomName;
    // 是否公开
    private Boolean isPublic;
    // 用户头像
    private List<String> users;
    // 最大用户数
    private Integer maxUser;
    // 是否是房主
    private Boolean isOwner;
}

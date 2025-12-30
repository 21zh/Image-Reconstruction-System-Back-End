package com.mxch.imgreconsturct.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomMsg implements Serializable {
    // 房间id
    private Integer roomId;
    // 用户id
    private String userId;
}

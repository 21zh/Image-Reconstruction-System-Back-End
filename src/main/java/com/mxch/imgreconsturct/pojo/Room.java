package com.mxch.imgreconsturct.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_room")
public class Room {
    // id
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    // 房间名称
    private String roomName;
    // 房间密码
    private String roomPassword;
    // 创建用户id
    private String userId;
    // 是否公开
    private Boolean isPublic;
    // 当前用户人数
    private Integer userNum;
    // 最大的用户人数
    private Integer maxUser;
    // 房间是否存在
    private Boolean isExist;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}

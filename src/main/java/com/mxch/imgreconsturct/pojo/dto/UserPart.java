package com.mxch.imgreconsturct.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPart {
    // 用户id
    private String id;
    // 用户名
    private String userName;
    // 用户头像
    private String avatar;
    // 个性签名
    private String motto;
    // 用户对象
    private Integer roleId;
}

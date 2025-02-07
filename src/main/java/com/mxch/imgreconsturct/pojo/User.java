package com.mxch.imgreconsturct.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_user")
public class User {
    // id
    @TableId(value = "id",type =  IdType.AUTO)
    private Integer id;
    // 用户名
    private String userName;
    // 密码
    private String password;
    // 手机号码
    private String phone;
    // 头像
    private String avatar;
    // 个性签名
    private String motto;
    // 用户角色(0：管理员，1：普通用户)
    private Integer roleId;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}

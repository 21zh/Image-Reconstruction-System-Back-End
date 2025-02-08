package com.mxch.imgreconsturct.pojo.dto;

import com.mxch.imgreconsturct.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm extends User {
    // 验证码
    private String code;
}

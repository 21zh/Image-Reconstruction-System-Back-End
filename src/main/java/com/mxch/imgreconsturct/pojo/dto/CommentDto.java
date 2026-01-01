package com.mxch.imgreconsturct.pojo.dto;

import com.mxch.imgreconsturct.pojo.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto extends Comment {
    // 用户头像
    private String userAvatar;
    // 用户名称
    private String userName;
}

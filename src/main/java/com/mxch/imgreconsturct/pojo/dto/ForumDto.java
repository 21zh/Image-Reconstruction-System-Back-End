package com.mxch.imgreconsturct.pojo.dto;

import com.mxch.imgreconsturct.pojo.Forum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumDto extends Forum {
    // 用户名
    String userName;
    // 头像
    String avatar;
    // 个性签名
    String motto;
    // 是否喜欢
    boolean ilike;
}

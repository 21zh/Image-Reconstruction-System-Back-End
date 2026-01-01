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
@TableName("tb_comment")
public class Comment {
    // id
    @TableId(value = "id",type =  IdType.AUTO)
    private Integer id;
    // 用户id
    private String userId;
    // 帖子id
    private String forumId;
    // 帖子内容
    private String commentMsg;
    // 创建时间
    private LocalDateTime createTime;
}

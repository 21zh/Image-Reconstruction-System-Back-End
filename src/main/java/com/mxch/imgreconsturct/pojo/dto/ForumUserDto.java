package com.mxch.imgreconsturct.pojo.dto;

import com.mxch.imgreconsturct.pojo.Forum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumUserDto{
    // 帖子列表
    private List<ForumDto> forumList;
    // 总点赞量
    private Integer allLikes;
    // 总下载量
    private Integer allDownloads;
}

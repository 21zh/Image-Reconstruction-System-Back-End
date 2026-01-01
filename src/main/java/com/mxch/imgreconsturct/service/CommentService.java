package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.Comment;
import com.mxch.imgreconsturct.pojo.dto.CommentDto;

import java.util.List;

public interface CommentService extends IService<Comment> {
    List<CommentDto> getForumComment(String forumId);

    void postForumComment(Comment comment);
}

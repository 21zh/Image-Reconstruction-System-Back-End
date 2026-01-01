package com.mxch.imgreconsturct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.CommentMapper;
import com.mxch.imgreconsturct.pojo.Comment;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.pojo.dto.CommentDto;
import com.mxch.imgreconsturct.service.CommentService;
import com.mxch.imgreconsturct.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private UserService userService;

    @Override
    public List<CommentDto> getForumComment(String forumId) {
        // 构建查询条件
        QueryWrapper<Comment> qw = new QueryWrapper<>();
        qw.eq("forum_id", forumId);
        qw.orderByDesc("create_time");

        List<Comment> commentList = commentMapper.selectList(qw);

        // 构造结果实体
        List<CommentDto> commentDtoList = commentList.stream().map(
                comment -> {
                    CommentDto commentDto = new CommentDto();

                    // 获取用户名称和用户头像
                    String userId = comment.getUserId();
                    User user = userService.getById(userId);
                    commentDto.setUserName(user.getUserName());
                    commentDto.setUserAvatar(user.getAvatar());

                    // 更新用户id为"",防止泄露
                    comment.setUserId("");
                    BeanUtils.copyProperties(comment, commentDto);

                    return commentDto;
                }
        ).collect(Collectors.toList());

        return commentDtoList;
    }

    @Override
    public void postForumComment(Comment comment) {
        // 更新帖子的评论数
        save(comment);
    }
}

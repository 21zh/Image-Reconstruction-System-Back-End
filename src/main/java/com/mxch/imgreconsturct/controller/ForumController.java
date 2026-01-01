package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.pojo.Comment;
import com.mxch.imgreconsturct.pojo.Forum;
import com.mxch.imgreconsturct.pojo.dto.CommentDto;
import com.mxch.imgreconsturct.service.CommentService;
import com.mxch.imgreconsturct.service.ForumService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Resource
    private ForumService forumService;

    @Resource
    private CommentService commentService;

    /**
     * 获取类型的帖子
     * @param typeId
     * @return
     */
    @GetMapping("/getTypeForum/{typeId}")
    public Result getTypeForum(@PathVariable("typeId") Integer typeId, @PathParam("keyWord") String keyWord,@PathParam("userName") String userName){
        return forumService.getTypeForum(typeId,keyWord,userName);
    }

    /**
     * 上传帖子
     * @param forum
     * @return
     */
    @PostMapping("/uploadForum")
    public Result uploadForum(@RequestBody Forum forum,@PathParam("userName") String userName){
        return forumService.uploadForum(forum,userName);
    }

    /**
     * 获取用户的详细信息
     * @param userId
     * @return
     */
    @GetMapping("/getUserForum/{userId}")
    public Result getUserInfo(@PathVariable("userId") Integer userId,@PathParam("userName") String userName){
        return forumService.getUserInfo(userId,userName);
    }

    /**
     * 获取帖子的评论信息
     * @param forumId
     * @return
     */
    @GetMapping("/getForumComment/{forumId}")
    public Result getForumComment(@PathVariable("forumId") String forumId) {
        try {
            List<CommentDto> commentList = commentService.getForumComment(forumId);
            return Result.ok(commentList);
        } catch (Exception e) {
            return Result.fail("评论获取失败");
        }
    }

    @PostMapping("/postForumComment")
    public Result postForumComment(@RequestBody Comment comment) {
        try {
            commentService.postForumComment(comment);
            return Result.ok();
        } catch (Exception e) {
            return Result.fail("评论发表失败");
        }
    }
}

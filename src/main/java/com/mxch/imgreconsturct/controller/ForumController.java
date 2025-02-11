package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.pojo.Forum;
import com.mxch.imgreconsturct.service.ForumService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

@Slf4j
@RestController
@RequestMapping("/forum")
public class ForumController {

    @Resource
    private ForumService forumService;

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
}

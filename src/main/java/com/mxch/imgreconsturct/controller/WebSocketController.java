package com.mxch.imgreconsturct.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mxch.imgreconsturct.pojo.Forum;
import com.mxch.imgreconsturct.pojo.Likes;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.service.ForumService;
import com.mxch.imgreconsturct.service.LikeService;
import com.mxch.imgreconsturct.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@Transactional
public class WebSocketController {

    @Resource
    private SimpMessagingTemplate messagingTemplate;
    @Resource
    private ForumService forumService;
    @Resource
    private UserService userService;

    @Resource
    private LikeService likeService;

    // 处理前端的下载请求
    @MessageMapping("/update")
    public void likeForum(@Payload String message) {
        // 使用map类型存储id和数据
        Map<String, Integer> map = new HashMap<>();
        // 解析其中的数据
        JSONObject jsonMessage = new JSONObject(message);
        Integer id = jsonMessage.getInt("id");
        String userName = jsonMessage.getStr("userName");
        String action = jsonMessage.getStr("action");
        Boolean iLike = jsonMessage.getBool("iLike");

        map.put("id",id);
        // 根据id查询该帖子的数据
        Forum forum = forumService.getById(id);
        log.info(String.valueOf(iLike));
        if (action.equals("likes")) {
            Integer likes = forum.getLikes();
            LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
            // 根据用户名查询id
            queryWrapper1.eq(User::getUserName,userName);
            User loginUser = userService.getOne(queryWrapper1);

            // 判断是点赞还是取消点赞
            if(iLike){
                // 取消点赞
                likes --;
                // 删除点赞的数据
                LambdaQueryWrapper<Likes> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Likes::getUserId,loginUser.getId())
                        .eq(Likes::getForumId,id);
                likeService.remove(queryWrapper);
            }else{
                // 点赞
                likes ++;
                // 新增数据库点赞数据
                Likes userLike = new Likes();
                userLike.setUserId(loginUser.getId());
                userLike.setForumId(id);
                likeService.save(userLike);
            }
            forum.setLikes(likes);
            // 更新数据库数据
            forumService.updateById(forum);
            map.put("likes",likes);
            // 推送新的点赞量
            messagingTemplate.convertAndSend("/forum/likes", map);
        } else if (action.equals("downloads")){
            // 下载事件
            // 更新下载量
            Integer downloads = forum.getDownloads() + 1;
            forum.setDownloads(downloads);
            // 更新数据库数据
            forumService.updateById(forum);
            map.put("downloads",downloads);
            // 推送新的下载量
            messagingTemplate.convertAndSend("/forum/downloads", map);
        } else if (action.equals("comments")) {
            // 评论事件
            // 更新评论数
            int comments = forum.getComments() + 1;
            forum.setComments(comments);
            // 更新数据库数据
            forumService.updateById(forum);
            map.put("comments", comments);
            // 推送新的评论数
            messagingTemplate.convertAndSend("/forum/comments", map);
        }
    }
}

package com.mxch.imgreconsturct.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.ForumMapper;
import com.mxch.imgreconsturct.mapper.LikeMapper;
import com.mxch.imgreconsturct.pojo.Forum;
import com.mxch.imgreconsturct.pojo.Likes;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.pojo.dto.ForumDto;
import com.mxch.imgreconsturct.pojo.dto.ForumUserDto;
import com.mxch.imgreconsturct.service.ForumService;
import com.mxch.imgreconsturct.service.UserService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mxch.imgreconsturct.util.RedisConstants.TOKEN_USER_KEY;

@Slf4j
@Service
@Transactional
public class ForumServiceImpl extends ServiceImpl<ForumMapper, Forum> implements ForumService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ForumMapper forumMapper;

    @Resource
    private LikeMapper likeMapper;
    @Resource
    private UserService userService;

    /**
     * 获取类型帖子的处理
     *
     * @param typeId
     * @param keyWord
     * @return
     */
    @Override
    public Result getTypeForum(Integer typeId, String keyWord,String userName) {
        LambdaQueryWrapper<Forum> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<User> queryWrapper2 = new LambdaQueryWrapper<>();
        // 如果id为0，则查询全部
        if (typeId == 0) {
            queryWrapper.like(Forum::getTitle, keyWord);
        } else {
            queryWrapper.eq(Forum::getTypeId, typeId)
                    .like(Forum::getTitle, keyWord);
        }
        // 查询当前登录的用户
        queryWrapper2.eq(User::getUserName,userName);
        User loginUser = userService.getOne(queryWrapper2);
        // 否则，根据id查询数据
        List<Forum> forumList = forumMapper.selectList(queryWrapper);
        List<ForumDto> forumDtoList = forumList.stream().map((item) -> {
            ForumDto forumDto = new ForumDto();
            User user = userService.getById(item.getUserId());
            if (user != null) {
                BeanUtils.copyProperties(item, forumDto);
                // 根据用户id和帖子id查询是否点赞
                QueryWrapper<Likes> queryWrapper1 = new QueryWrapper<Likes>()
                        .eq("user_id", loginUser.getId())   // 确保字段名正确
                        .eq("forum_id", item.getId());      // 确保字段名正确

                Integer count = likeMapper.selectCount(queryWrapper1);
                log.info("用户id" + loginUser.getId());
                log.info("帖子id"+ item.getId());
                log.info("是否点赞：" + count.toString());
                forumDto.setIlike(count == 1);
                forumDto.setUserName(user.getUserName());
                forumDto.setAvatar(user.getAvatar());
                forumDto.setMotto(user.getMotto());
            }
            return forumDto;
        }).collect(Collectors.toList());
        return Result.ok(forumDtoList);
    }

    @Override
    public Result uploadForum(Forum forum, String userName) {
        // 根据用户名查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        User user = userService.getOne(queryWrapper);

        // 将部分属性赋值给帖子对象
        forum.setUserId(user.getId());
        forum.setCreateTime(LocalDateTime.now());

        // 添加进数据库
        save(forum);

        return Result.ok();
    }

    /**
     * 获取用户的帖子和其他信息的逻辑处理
     * @param userId
     * @return
     */
    @Override
    public Result getUserInfo(Integer userId,String userName) {
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Likes> queryWrapper = new LambdaQueryWrapper<>();
        // 根据用户名查询用户
        queryWrapper1.eq(User::getUserName,userName);
        User nowUser = userService.getOne(queryWrapper1);
        // 定义一个帖子用户对象
        ForumUserDto forumUserDto = new ForumUserDto();
        // 根据id查询用户信息
        User user = userService.getById(userId);
        // 根据id查询用户所发的所有帖子
        List<Forum> userForum = query().eq("user_id", userId).list();
        // 填充帖子列表
        List<ForumDto> forumDtoList = userForum.stream().map(item -> {
            ForumDto forumDto = new ForumDto();
            BeanUtils.copyProperties(item, forumDto);
            // 配置用户属性
            forumDto.setUserName(user.getUserName());
            forumDto.setMotto(user.getMotto());
            forumDto.setAvatar(user.getAvatar());
            queryWrapper.eq(Likes::getUserId, nowUser.getId())
                    .eq(Likes::getForumId, item.getId());
            Integer count = likeMapper.selectCount(queryWrapper);
            forumDto.setIlike(count == 1);
            return forumDto;
        }).collect(Collectors.toList());
        // 所有的点赞数和下载数
        Integer allLikes = 0;
        Integer allDownloads = 0;
        // 遍历获取
        for (Forum forum : userForum) {
            allLikes += forum.getLikes();
            allDownloads += forum.getDownloads();
        }
        // 将数据进行封装
        forumUserDto.setForumList(forumDtoList);
        forumUserDto.setAllLikes(allLikes);
        forumUserDto.setAllDownloads(allDownloads);
        // 返回结果
        return Result.ok(forumUserDto);
    }
}

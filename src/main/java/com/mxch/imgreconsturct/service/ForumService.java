package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.Forum;
import com.mxch.imgreconsturct.util.Result;

import javax.servlet.http.HttpServletRequest;

public interface ForumService extends IService<Forum> {

    public Result getTypeForum(Integer typeId,String keyWord);

    public Result uploadForum(Forum forum, String userName);
}

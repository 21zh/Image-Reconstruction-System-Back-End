package com.mxch.imgreconsturct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.util.Result;

public interface AdminService extends IService<User> {
    public Result getUserInfo(Integer pageNo, Integer pageSize, String keyWord);

    public Result changeRole(Integer id);

    public Result updateUser(User user);

    public Result addUser(User user);

    public Result deleteUser(Integer id);
}

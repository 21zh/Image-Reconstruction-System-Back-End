package com.mxch.imgreconsturct.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mxch.imgreconsturct.mapper.AdminMapper;
import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.pojo.dto.PageDto;
import com.mxch.imgreconsturct.service.AdminService;
import com.mxch.imgreconsturct.util.MD5;
import com.mxch.imgreconsturct.util.Result;
import com.mxch.imgreconsturct.util.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
public class AdminServiceImpl extends ServiceImpl<AdminMapper, User> implements AdminService {

    @Resource
    private AdminMapper adminMapper;
    /**
     * 用户分页查询逻辑处理
     * @param pageNo
     * @param pageSize
     * @param keyWord
     * @return
     */
    @Override
    public Result getUserInfo(Integer pageNo, Integer pageSize, String keyWord) {
        // 创建分页查询
        Page<User> page = new Page<>(pageNo,pageSize);
        // 进行分页查询
        Page<User> pageUser = adminMapper.selectPageByKeyword(page,keyWord);
        // 创建存储数据的分页对象
        // 将分页获取的结果进行赋值
        PageDto userInfos = BeanUtil.copyProperties(pageUser, PageDto.class, "records");
        // 获取用户列表
        List<User> records = pageUser.getRecords();
        // 赋值
        userInfos.setUserList(records);
        return Result.ok(userInfos);
    }

    /**
     * 改变用户角色逻辑处理
     * @param id
     * @return
     */
    @Override
    public Result changeRole(Integer id) {
        // 根据id查询用户对象
        User user = adminMapper.selectById(id);
        // 判断用户的角色类型
        Integer roleId = user.getRoleId();
        // 如果为管理员，则改为普通用户
        if(roleId == 0){
            user.setRoleId(1);
        }else{
            // 否则，改为管理员
            user.setRoleId(0);
        }
        user.setUpdateTime(LocalDateTime.now());
        // 更新数据库数据
        updateById(user);
        return Result.ok();
    }

    /**
     * 更新用户
     * @param user
     * @return
     */
    @Override
    public Result updateUser(User user) {
        // 根据id查询用户
        User orginUser = adminMapper.selectById(user.getId());

        if(!user.getPassword().equals(orginUser.getPassword())){
            // 对用户密码进行加密
            String encryptPassword = MD5.encrypt(user.getPassword());
            user.setPassword(encryptPassword);
        }
        // 将更新的数据进行赋值
        BeanUtils.copyProperties(user,orginUser);
        orginUser.setUpdateTime(LocalDateTime.now());

        // 更新数据库中的用户数据
        updateById(orginUser);
        return Result.ok();
    }

    /**
     * 新增用户逻辑处理
     * @param user
     * @return
     */
    @Override
    public Result addUser(User user) {
        // 判断用户名是否存在
        String userName = user.getUserName();
        User user1 = query().eq("user_name", userName).one();
        // 用户名已经存在
        if(user1 != null){
            return Result.build(null, ResultCodeEnum.ISUSED_USERNAME);
        }

        // 手机号是否存在
        String phone = user.getPhone();
        User user2 = query().eq("phone", phone).one();
        // 已存在
        if(user2 != null){
            return Result.build(null,ResultCodeEnum.ISUSED_PHONE);
        }

        // 对用户密码进行加密
        String encryptPassword = MD5.encrypt(user.getPassword());
        user.setPassword(encryptPassword);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        // 将用户添加到数据库中
        save(user);

        return Result.ok();
    }

    /**
     * 删除用户的逻辑处理
     * @param id
     * @return
     */
    @Override
    public Result deleteUser(Integer id) {
        // 根据id查询用户对象
        User user = adminMapper.selectById(id);
        // 用户为空
        if(user == null){
            return Result.fail();
        }
        // 用户不为空
        // 删除数据库中的用户数据
        adminMapper.deleteById(id);
        return Result.ok();
    }
}

package com.mxch.imgreconsturct.controller;

import com.mxch.imgreconsturct.pojo.User;
import com.mxch.imgreconsturct.service.AdminService;
import com.mxch.imgreconsturct.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminService adminService;

    /**
     * 获取用户信息
     *
     * @param pageNo
     * @param pageSize
     * @param keyWord
     * @return
     */
    @GetMapping("/getUserInfo/{pageNo}/{pageSize}")
    public Result getUserInfo(@PathVariable("pageNo") Integer pageNo,
                              @PathVariable("pageSize") Integer pageSize,
                              @RequestParam(required = false) String keyWord) {
        return adminService.getUserInfo(pageNo, pageSize, keyWord);
    }

    /**
     * 改变用户的角色
     * @param id
     * @return
     */
    @PutMapping("/changeRole/{id}")
    public Result changeRole(@PathVariable("id") Integer id){
        return adminService.changeRole(id);
    }

    /**
     * 更新用户数据
     * @param user
     * @return
     */
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody User user){
        return adminService.updateUser(user);
    }

    /**
     * 新增用户数据
     * @param user
     * @return
     */
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user){
        return adminService.addUser(user);
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUser(@PathVariable("id") Integer id){
        return adminService.deleteUser(id);
    }
}

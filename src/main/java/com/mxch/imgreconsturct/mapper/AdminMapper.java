package com.mxch.imgreconsturct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mxch.imgreconsturct.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper extends BaseMapper<User> {
    public Page<User> selectPageByKeyword(Page<User> page, @Param("keyWord") String keyWord);
}

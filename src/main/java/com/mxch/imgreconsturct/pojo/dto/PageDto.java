package com.mxch.imgreconsturct.pojo.dto;

import com.mxch.imgreconsturct.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDto {
    // 用户信息
    private List<User> userList;
    // 总数
    private Integer total;
    // 每页记录数
    private Integer pageSize;
    // 当前页码
    private Integer pageNo;
    // 总页数
    private Integer totalPage;
}

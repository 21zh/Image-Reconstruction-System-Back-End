package com.mxch.imgreconsturct.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrokeDto {
    // 用户id
    private String userId;
    // 房间id
    private String roomId;
    // 操作类型
    private String type;
    // 线宽度
    private Integer lineWidth;
    // x、y点
    private List<PointDto> points;
}

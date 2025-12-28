package com.mxch.imgreconsturct.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_stroke")
public class Stroke {
    // id
    @TableId(value = "id",type =  IdType.AUTO)
    private Integer id;
    // 用户id
    private String userId;
    // 房间id
    private String roomId;
    // 操作类型
    private String type;
    // 线宽度
    private Integer lineWidth;
    // x、y点
    private String points;
    // 操作时间
    private LocalDateTime opearteTime;
}

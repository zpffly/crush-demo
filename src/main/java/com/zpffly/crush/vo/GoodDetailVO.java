package com.zpffly.crush.vo;

import com.zpffly.crush.entity.User;
import lombok.Data;

@Data
public class GoodDetailVO {
    private int crushStatus;
    private int remainSecond;
    GoodsVO good;
    User user;
}

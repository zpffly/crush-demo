package com.zpffly.crush.controller;

import com.zpffly.crush.entity.CrushOrderInfo;
import com.zpffly.crush.entity.Result;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.myenum.CrushEnum;
import com.zpffly.crush.service.GoodsService;
import com.zpffly.crush.service.OrderService;
import com.zpffly.crush.vo.GoodsVO;
import com.zpffly.crush.vo.OrderDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderDetailController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/detail")
    public Result getDetail(User user, @RequestParam("orderId") long orderId){
        if (user == null)
            return Result.error(CrushEnum.USER_NOT_FOUND);
        CrushOrderInfo order = orderService.getOrderInfoById(orderId);
        if (order == null)
            return Result.error(CrushEnum.ORDER_NOT_EXIST);
        long goodsId = order.getGoodsId();
        GoodsVO goods = goodsService.getGoodVOByGoodId(goodsId);
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setGoodsVO(goods);
        orderDetailVO.setOrder(order);
        return Result.success(orderDetailVO);
    }
}

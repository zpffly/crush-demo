package com.zpffly.crush.controller;

import com.zpffly.crush.entity.Result;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.service.GoodsService;
import com.zpffly.crush.service.UserService;
import com.zpffly.crush.vo.GoodDetailVO;
import com.zpffly.crush.vo.GoodsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/good")
@Slf4j
public class GoodController {

    @Autowired
    private UserService userService;
    @Autowired
    GoodsService goodsService;



    @GetMapping("/list")
    public String toList(Model model, User user){
        // 用户不存在，重新登录
//        if (user == null)
//            return "login";
        model.addAttribute("user", user);
        // 得到商品列表
        List<GoodsVO> goodsList = goodsService.getGoodVOList();
        model.addAttribute("goodsList", goodsList);
        return "good_list";
    }

//    @GetMapping("/detail/{goodId}")
//    public String toDetail(Model model, User user,
//                           @PathVariable("goodId") long goodId){
//        model.addAttribute("user", user);
//        GoodsVO good = goodsService.getGoodVOByGoodId(goodId);
//        model.addAttribute("good", good);
//
//        long startTime = good.getStartDate().getTime();
//        long endTime = good.getEndDate().getTime();
//        long now = System.currentTimeMillis();
//        // 秒杀状态
//        int crushStatus = 0;
//        // 剩余时间
//        int remainSecond = 0;
//        if (now < startTime){ // 秒杀还没有开始
//            remainSecond = (int)(startTime - now) / 1000;
//        }else if (now > endTime){ //结束
//            crushStatus = 2;
//            remainSecond = -1;
//        }else {
//            crushStatus = 1;
//        }
//
//        model.addAttribute("crushStatus", crushStatus);
//        model.addAttribute("remainSecond", remainSecond);
//
//        return "goods_detail";
//    }

    @GetMapping("/detail/{goodId}")
    @ResponseBody
    public Result<GoodDetailVO> toDetail(User user,
                                         @PathVariable("goodId") long goodId){
        System.out.println("================================");
        GoodsVO good = goodsService.getGoodVOByGoodId(goodId);
        long startTime = good.getStartDate().getTime();
        long endTime = good.getEndDate().getTime();
        long now = System.currentTimeMillis();
        // 秒杀状态
        int crushStatus = 0;
        // 剩余时间
        int remainSecond = 0;
        if (now < startTime){ // 秒杀还没有开始
            remainSecond = (int)(startTime - now) / 1000;
        }else if (now > endTime){ //结束
            crushStatus = 2;
            remainSecond = -1;
        }else {
            crushStatus = 1;
        }
        GoodDetailVO goodDetailVO = new GoodDetailVO();
        goodDetailVO.setCrushStatus(crushStatus);
        goodDetailVO.setRemainSecond(remainSecond);
        goodDetailVO.setGood(good);
        goodDetailVO.setUser(user);
        return Result.success(goodDetailVO);
    }
}

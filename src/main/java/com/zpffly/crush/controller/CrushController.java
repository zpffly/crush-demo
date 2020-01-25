package com.zpffly.crush.controller;

import com.zpffly.crush.constant.RedisConstant;
import com.zpffly.crush.entity.CrushOrder;
import com.zpffly.crush.entity.CrushOrderInfo;
import com.zpffly.crush.entity.Result;
import com.zpffly.crush.entity.User;
import com.zpffly.crush.myenum.CrushEnum;
import com.zpffly.crush.rabbitmq.RabbitMQSender;
import com.zpffly.crush.rabbitmq.RabbitmqMessage;
import com.zpffly.crush.service.CrushService;
import com.zpffly.crush.service.GoodsService;
import com.zpffly.crush.service.OrderService;
import com.zpffly.crush.vo.GoodsVO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/crush")
public class CrushController implements InitializingBean{

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CrushService crushService;
    @Autowired
    RabbitMQSender rabbitMQSender;
    @Autowired
    RedisTemplate<String, Integer> redisTemplate;

    /* 根据商品id判断是否秒杀结束 */
    private static volatile HashMap<Long, Boolean> isSaleOver = new HashMap<>();

    /**
     * 加载所有秒杀的商品库存到redis
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVO> goods = goodsService.getGoodVOList();
        if (goods == null)
            return;
        for (GoodsVO good : goods){
            // 不设置过期时间,秒杀结束后统一处理
            redisTemplate.opsForValue().set(RedisConstant.GOODS_STOCK+good.getId(), good.getStockCount());
            isSaleOver.put(good.getId(), false);
        }
    }

//    @PostMapping("/doCrush")
//    public String doCrush(Model model, User user,
//                          @RequestParam("goodId") long goodId){
//        if (user == null)
//            return "login";
//        model.addAttribute("user", user);
//        // 获取商品判断库存
//        GoodsVO good = goodsService.getGoodVOByGoodId(goodId);
//        // 获取商品秒杀的库存
//        int stock = good.getStockCount();
//        if (stock <= 0){
//            model.addAttribute("msg", CrushEnum.STOCK_LOWER_ZERO.getMsg());
//            return "crush_fail";
//        }
//        // 如果有库存，还要判断用户是否已经秒杀过这个商品
//        CrushOrder order = orderService.getOrderByUserIdAndGoodId(user.getId(), goodId);
//        if (order != null){
//            model.addAttribute("msg", CrushEnum.GOOD_CRUSH_REPEAT.getMsg());
//            return "crush_fail";
//        }
//        // 可以秒杀
//        CrushOrderInfo orderInfo = crushService.doCrush(user, good);
//        model.addAttribute("orderInfo", orderInfo);
//        model.addAttribute("good", good);
//        return "order_detail";
//
//    }


    @PostMapping("/{path}/doCrush")
    @Transactional
    public Result<CrushOrderInfo> doCrush(User user,
                          @RequestParam("goodId") long goodId,
                          @PathVariable("path") String path){
        if (user == null)
            return Result.error(CrushEnum.USER_NOT_FOUND);

        // 验证path
        boolean check = crushService.checkPath(user, goodId, path);
        if (!check)
            return Result.error(CrushEnum.PATH_ERROR);
        // 先看商品是否售完
        if (isSaleOver.get(goodId))
            return Result.error(CrushEnum.STOCK_LOWER_ZERO);

        // 减少redis中相应商品的库存
        Long remainStock = redisTemplate.opsForValue().decrement(RedisConstant.GOODS_STOCK + goodId);
        if (remainStock == null || remainStock < 0){
            // 设置商品售完
            isSaleOver.put(goodId, true);
            return Result.error(CrushEnum.STOCK_LOWER_ZERO);
        }
        // 判断用户是否已经秒杀过这个商品
        CrushOrder order = orderService.getOrderByUserIdAndGoodId(user.getId(), goodId);
        if (order != null){
            // 如果判断用户已经有了订单,减预减的库存增加回来
            redisTemplate.opsForValue().increment(RedisConstant.GOODS_STOCK + goodId);
            isSaleOver.put(goodId, false);
            return Result.error(CrushEnum.GOOD_CRUSH_REPEAT);
        }

        RabbitmqMessage message = new RabbitmqMessage();
        message.setUser(user);
        message.setGoodId(goodId);
        rabbitMQSender.send(message);
        /*
        // 获取商品判断库存
        GoodsVO good = goodsService.getGoodVOByGoodId(goodId);
        // 获取商品秒杀的库存
        int stock = good.getStockCount();
        if (stock <= 0){
            return Result.error(CrushEnum.STOCK_LOWER_ZERO);
        }
        // 如果有库存，还要判断用户是否已经秒杀过这个商品
        CrushOrder order = orderService.getOrderByUserIdAndGoodId(user.getId(), goodId);
        if (order != null){
            return Result.error(CrushEnum.GOOD_CRUSH_REPEAT);
        }
        // 可以秒杀
        CrushOrderInfo orderInfo = crushService.doCrush(user, good);
        return Result.success(orderInfo);

         */
        // 返回正在处理订单
        return Result.success(CrushEnum.CONFIRM_ORDER_ING);
    }

    @GetMapping("/result")
    public Result crushResult(User user, @RequestParam("goodId") long goodId){
        if (user == null)
            return Result.error(CrushEnum.USER_NOT_FOUND);
        return crushService.getCrushResult(user.getId(), goodId);
    }

    @GetMapping("/path")
    public Result path(User user, @RequestParam("goodId") long goodId){
        if (user == null)
            return Result.error(CrushEnum.USER_NOT_FOUND);
        String path = crushService.createCrushPath(user, goodId);
        return Result.success(path);
    }


}

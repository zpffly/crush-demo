package com.zpffly.crush.dao;

import com.zpffly.crush.entity.CrushOrder;
import com.zpffly.crush.entity.CrushOrderInfo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderDao {

    @Select("SELECT * FROM crush_order WHERE user_id =  #{userId} AND goods_id = #{goodId};")
    CrushOrder getOrderByUserIdAndGoodId(@Param("userId") long userId, @Param("goodId") long goodId);
    @Insert("insert into crush_order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date) " +
            " values( #{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate} )")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")//必须有，这样java对象才能得到自增长的值
    long insertOrderInfo(CrushOrderInfo orderInfo);

    @Insert("insert into crush_order (user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    @SelectKey(keyColumn="id", keyProperty="id", resultType=long.class, before=false, statement="select last_insert_id()")
    int insertOrder(CrushOrder crushOrder);

    @Select("select * from crush_order where id = #{orderId}")
    CrushOrderInfo getOrderInfoById(@Param("orderId") long orderId);
}

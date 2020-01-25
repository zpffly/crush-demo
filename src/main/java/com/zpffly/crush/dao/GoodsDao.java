package com.zpffly.crush.dao;

import com.zpffly.crush.entity.Goods;
import com.zpffly.crush.vo.GoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface GoodsDao {

    @Select("SELECT g.*, cg.stock_count, cg.start_date, cg.end_date, cg.crush_price\n" +
            "FROM goods g LEFT JOIN crush_goods cg\n" +
            "on g.id = cg.goods_id;")
    List<GoodsVO> getGoodVOList();

    @Select("SELECT g.*, cg.stock_count, cg.start_date, cg.end_date, cg.crush_price\n" +
            "FROM goods g LEFT JOIN crush_goods cg\n" +
            "on g.id = cg.goods_id\n" +
            "WHERE g.id = #{goodId};")
    GoodsVO getGoodVOByGoodId(long goodId);

    @Update("UPDATE crush_goods SET stock_count = stock_count-1 WHERE goods_id = #{goodId} and stock_count > 0;")
    int reduceCrushGoodStock(@Param("goodId") long goodId);
}

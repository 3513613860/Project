package org.example.Dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.entity.MiaoshaGoods;
import org.example.vo.GoodsVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface GoodsDao {
    @Select("select g.*,mg.stock_count,mg.start_date,mg.miaosha_price,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id=g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.miaosha_price,mg.end_date from miaosha_goods mg left join goods g on mg.goods_id=g.id where g.id=#{goodsId}")
    GoodsVo getGoodsVoByGoodsId(@Param("goodsId") long goodsId);

    @Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(MiaoshaGoods g);
}

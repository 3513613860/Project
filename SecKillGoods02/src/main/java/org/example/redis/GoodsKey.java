package org.example.redis;

public class GoodsKey extends BasePrefix{

    public GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }


    //页面缓存的有效期，60s，为什么设置60s呢，因为页面在不断变化，60s相对来说比较合理的
    public  static  GoodsKey getGoodsList = new GoodsKey(60,"gl");
    public  static  GoodsKey getGoodsDetail = new GoodsKey(60,"gd");
    public  static  GoodsKey getMiaoshaGoodsStock = new GoodsKey(0,"gs");
}

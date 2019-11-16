package com.atguigu.gmall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.cart.feign.GmallPmsClient;
import com.atguigu.gmall.cart.feign.GmallSmsClient;
import com.atguigu.gmall.cart.interceptor.LoginInterceptor;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.cart.vo.Cart;
import com.atguigu.core.bean.UserInfo;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.sms.vo.ItemSaleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private GmallPmsClient gmallPmsClient;
    @Autowired
    private GmallSmsClient gmallSmsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "cart:key:";

    private static final String CURRENT_PRICE_PREFIX = "cart:price:";

    /**
     * 添加的实现
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {

        String key = getKey();

        //判断购物车当中是否有该记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);
        Integer count = cart.getCount(); // 取出用户新增购物车商品的数量
        String skuId = cart.getSkuId().toString();
        //有更新数量
        if (hashOperations.hasKey(skuId)){
            String cartJson = hashOperations.get(skuId).toString();
            cart = JSON.parseObject(cartJson,Cart.class);
            //更新数量
            cart.setCount(cart.getCount() + count);

        }else {
            //没有，新增记录
            Resp<SkuInfoEntity> skuInfoEntityResp = this.gmallPmsClient.querySkuById(cart.getSkuId());
            SkuInfoEntity skuInfoEntity = skuInfoEntityResp.getData();

            cart.setTitle(skuInfoEntity.getSkuTitle());

            cart.setCheck(true);

            cart.setPrice(skuInfoEntity.getPrice());
            // 查询销售属性
            Resp<List<SkuSaleAttrValueEntity>> listResp = this.gmallPmsClient.querySaleAttrBySkuId(cart.getSkuId());
            List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = listResp.getData();
            cart.setSkuSaleAttrValue(skuSaleAttrValueEntities);

            cart.setDefaultImage(skuInfoEntity.getSkuDefaultImg());
            //查询营销信息
            Resp<List<ItemSaleVO>> listResp1 = this.gmallSmsClient.queryItemSaleVOS(cart.getSkuId());
            List<ItemSaleVO> itemSaleVOS = listResp1.getData();
            cart.setSales(itemSaleVOS);
            this.redisTemplate.opsForValue().set(CURRENT_PRICE_PREFIX + skuId ,skuInfoEntity.getPrice().toString());

        }
        //同步到redis中
        hashOperations.put(skuId,JSON.toJSONString(cart));


    }

    /**
     * 查询的实现
     * @return
     */
    @Override
    public List<Cart> queryCarts() {

        //查询未登录状态的购物车
        UserInfo userInfo = LoginInterceptor.get();
        String key1 = KEY_PREFIX + userInfo.getUserKey();

        BoundHashOperations<String, Object, Object> userKeyOps = this.redisTemplate.boundHashOps(key1);
        List<Object> cartJsonList = userKeyOps.values();
        List<Cart> userKeyCarts = null;
        //判断购物车是否为空
        if (!CollectionUtils.isEmpty(cartJsonList)){
            userKeyCarts = cartJsonList.stream().map(cartJson ->{
                Cart cart = JSON.parseObject(cartJson.toString(), Cart.class);
                cart.setCurrentPrice(new BigDecimal(this.redisTemplate.opsForValue().get(CURRENT_PRICE_PREFIX+cart.getSkuId())));
                return cart;
            }).collect(Collectors.toList());



        }
        //判断登录状态
        if (userInfo.getUserId() == null){
            //未登录直接返回
            return userKeyCarts;
        }

        //登录， 查询登录状态的购物车
        String key2 = KEY_PREFIX + userInfo.getUserId();
        BoundHashOperations<String, Object, Object> userIdOps = this.redisTemplate.boundHashOps(key2);
        //判断未登录状态的购物车是否为空
        if (!CollectionUtils.isEmpty(userKeyCarts)){
            //不为空，合并
            userKeyCarts.forEach(cart ->{
                //有更新数量
                if (userIdOps.hasKey(cart.getSkuId().toString())){
                    String cartJson = userIdOps.get(cart.getSkuId().toString()).toString();
                    Cart idCart = JSON.parseObject(cartJson,Cart.class);
                    //更新数量
                    idCart.setCount(idCart.getCount() + cart.getCount());
                    userIdOps.put(cart.getSkuId().toString(),JSON.toJSONString(idCart));
                }else {
                    //没有，新增记录
                    userIdOps.put(cart.getSkuId().toString(),JSON.toJSONString(cart));
                }
            });
            this.redisTemplate.delete(key1);
        }
        //为空，直接返回登录状态的购物车
        List<Object> userIdCartJsonList = userIdOps.values();
        if (CollectionUtils.isEmpty(userIdCartJsonList)){
            return null;
        }
        return userIdCartJsonList.stream().map(userIdCartJson -> {
            Cart cart = JSON.parseObject(userIdCartJson.toString(), Cart.class);
            String s = this.redisTemplate.opsForValue().get(CURRENT_PRICE_PREFIX + cart.getSkuId());
            cart.setCurrentPrice(new BigDecimal(s));
            return cart;
        }).collect(Collectors.toList());
    }

    /**
     * 更改的实现
     * @param cart
     */
    @Override
    public void updateCart(Cart cart) {

        String key = getKey();

        Integer count = cart.getCount();

        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        if (hashOps.hasKey(cart.getSkuId().toString())){
            //获取购物车中的更新数量的购物记录
            String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
            cart = JSON.parseObject(cartJson,Cart.class);
            cart.setCount(cart.getCount());
            hashOps.put(cart.getSkuId().toString(),JSON.toJSONString(cart));
        }

    }

    /**
     * 删除的实现
     * @param skuId
     */
    @Override
    public void deleteCart(Long skuId) {

        String key = getKey();

        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);
        if (hashOps.hasKey(skuId.toString())){
            hashOps.delete(skuId.toString());
        }
    }

    /**
     * 添加勾选项的实现
     * @param carts
     */
    @Override
    public void checkCart(List<Cart> carts) {
        String key = getKey();
        BoundHashOperations<String, Object, Object> hashOps = this.redisTemplate.boundHashOps(key);

        carts.forEach(cart -> {
            Boolean check = cart.getCheck();
            if (hashOps.hasKey(cart.getSkuId().toString())){
                //获取购物车中的更新数量的购物记录
                String cartJson = hashOps.get(cart.getSkuId().toString()).toString();
                cart = JSON.parseObject(cartJson,Cart.class);
                cart.setCheck(check);
                hashOps.put(cart.getSkuId().toString(),JSON.toJSONString(cart));
            }
        });

    }


    /**
     * 判断登录状态，获取key的提取方法
     * @return
     */
    private String getKey() {
        String key = KEY_PREFIX;
        //判断登录状态
        UserInfo userInfo = LoginInterceptor.get();
        if (userInfo.getUserId() != null){
            key += userInfo.getUserId();
        }else {
            key += userInfo.getUserKey();
        }
        return key;
    }
}

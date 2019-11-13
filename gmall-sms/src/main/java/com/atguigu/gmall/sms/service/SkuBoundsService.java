package com.atguigu.gmall.sms.service;

import com.atguigu.gmall.sms.vo.ItemSaleVO;
import com.atguigu.gmall.sms.vo.SaleVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 商品sku积分设置
 *
 * @author qujiye
 * @email 825887605@qq.com
 * @date 2019-10-29 00:38:00
 */
public interface SkuBoundsService extends IService<SkuBoundsEntity> {

    PageVo queryPage(QueryCondition params);

    void saveSale(SaleVO saleVO);

    List<ItemSaleVO> queryItemSaleVOS(Long skuId);
}


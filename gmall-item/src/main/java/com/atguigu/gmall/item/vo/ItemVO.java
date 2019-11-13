package com.atguigu.gmall.item.vo;

import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.GroupVO;
import com.atguigu.gmall.sms.vo.ItemSaleVO;
import lombok.Data;

import java.util.List;

@Data
public class ItemVO extends SpuInfoEntity {

    //品牌
    private BrandEntity brand;
    //分类
    private CategoryEntity category;
    //sku的图片列表
    private List<String> pics;
    //优惠和促销信息
    private List<ItemSaleVO> sales;
    //是否有货
    private boolean store;
    //spu下所有sku的销售属性值
    private List<SkuSaleAttrValueEntity> skuSales;
    //商品描述信息
    private SpuInfoDescEntity desc;
    //组下的规格参数和值
    private List<GroupVO> groups;
    //spu描述信息
    private SpuInfoEntity spuInfo;


}

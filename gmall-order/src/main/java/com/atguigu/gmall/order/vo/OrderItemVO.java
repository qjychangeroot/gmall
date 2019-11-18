package com.atguigu.gmall.order.vo;

import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import com.atguigu.gmall.sms.vo.ItemSaleVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemVO {

    private Long skuId;//商品id
    private String title;//标题
    private String defaultImage;//图片
    private BigDecimal price; //当前价格
    private Integer count;//购买数量
    private Boolean store;//是否有货
    private List<SkuSaleAttrValueEntity> skuSaleAttrValue;//商品规格参数（销售属性）
    private List<ItemSaleVO> sales;//营销信息
    private BigDecimal weigh;//商品重量


}

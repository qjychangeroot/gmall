package com.atguigu.gmall.order.vo;

import com.atguigu.gmall.ums.entity.MemberReceiveAddressEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSubmitVO {

    private MemberReceiveAddressEntity address;//收货地址

    private Integer payType;//支付方式

    private String deliveryCompany;//物流公司（配送方式）

    private List<OrderItemVO> orderItemVOS;//订单详情

    private Integer userIntegration;//下单使用的积分

    private BigDecimal totalPrice;//总价，用于验价

    private String orderToken;//防重，订单编号

}

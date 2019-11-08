package com.atguigu.gmall.oms.dao;

import com.atguigu.gmall.oms.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author qujiye
 * @email 825887605@qq.com
 * @date 2019-10-29 00:35:48
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}

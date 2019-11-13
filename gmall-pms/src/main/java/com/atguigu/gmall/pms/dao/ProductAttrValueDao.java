package com.atguigu.gmall.pms.dao;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.atguigu.gmall.pms.vo.ProductAttrValueVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * spu属性值
 * 
 * @author qujiye
 * @email 825887605@qq.com
 * @date 2019-10-29 00:28:44
 */
@Mapper
public interface ProductAttrValueDao extends BaseMapper<ProductAttrValueEntity> {

    List<ProductAttrValueEntity> querySerchAttrValue(Long spuId);

    List<ProductAttrValueEntity> queryByGidAndSpuId(@Param("groupId") Long groupId, @Param("spuId") Long spuId);
}

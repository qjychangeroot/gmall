package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import lombok.Data;

import java.util.List;

@Data
public class GroupVO {
    //组的名称
    private String groupName;
    //规格属性和值
    private List<ProductAttrValueEntity> baseAttrValues;
}

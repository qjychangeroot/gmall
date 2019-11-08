package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.pms.vo.AttrGroupVO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;

import java.util.List;


/**
 * 属性分组
 *
 * @author qujiye
 * @email 825887605@qq.com
 * @date 2019-10-29 00:28:44
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageVo queryPage(QueryCondition params);

    PageVo queryByCidPage(Long catId, QueryCondition queryCondition);

    AttrGroupVO queryGroupWithAttrs(Long gid);

    List<AttrGroupVO> queryGroupWithAttrsByCid(Long catId);
}


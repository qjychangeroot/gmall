package com.atguigu.gmall.pms.api;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import com.atguigu.gmall.pms.vo.CategoryVO;
import com.atguigu.gmall.pms.vo.SpuAttributeValueVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface GmallPmsApi {

//    分页查询spu
    @PostMapping("pms/spuinfo/list")
    public Resp<List<SpuInfoEntity>> querySpuPage(@RequestBody QueryCondition queryCondition);

//    根据spuId查询sku
    @GetMapping("pms/skuinfo/{spuId}")
    public Resp<List<SkuInfoEntity>> querySkuBySpuId(@PathVariable("spuId")Long spuId);

//    根据brandId查询品牌
    @GetMapping("pms/brand/info/{brandId}")
    public Resp<BrandEntity> queryBrandById(@PathVariable("brandId") Long brandId);

//    根据categoryId查询分类
    @GetMapping("pms/category/info/{catId}")
    public Resp<CategoryEntity> queryCategoryById(@PathVariable("catId") Long catId);

//    根据spuId查询检索属性
    @GetMapping("pms/productattrvalue/{spuId}")
    public Resp<List<SpuAttributeValueVO>> querySearchAttrValue(@PathVariable("spuId")Long spuId);

//    商品三级分类
    @GetMapping("pms/category")
    public Resp<List<CategoryEntity>> queryCategories(@RequestParam(value = "level",defaultValue = "0")Integer level, @RequestParam(value = "parentCid",required = false)Integer parentCid);

//    查询商品分类的二级节点和三级节点
    @GetMapping("pms/category/{pid}")
    public Resp<List<CategoryVO>> queryCategoryWithSub(@PathVariable("pid")Long pid);

}

package com.atguigu.gmall.pms.api;

import com.atguigu.core.bean.QueryCondition;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.CategoryVO;
import com.atguigu.gmall.pms.vo.GroupVO;
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

//    根据skuId查询sku
    @GetMapping("pms/skuinfo/info/{skuId}")
    public Resp<SkuInfoEntity> querySkuById(@PathVariable("skuId") Long skuId);

//    根据spuId查询spu
    @GetMapping("pms/spuinfo/info/{id}")
    public Resp<SpuInfoEntity> querySpuById(@PathVariable("id") Long id);

//    根据skuId查询图片
    @GetMapping("pms/skuimages/{skuId}")
    public Resp<List<String>> queryPicsById(@PathVariable("skuId")Long skuId);

//    查询
    @GetMapping("pms/skusaleattrvalue/{spuId}")
    public Resp<List<SkuSaleAttrValueEntity>> querySaleAttrValues(@PathVariable("spuId")Long spuId);

//    查询商品描述信息
    @GetMapping("pms/spuinfodesc/info/{spuId}")
    public Resp<SpuInfoDescEntity> querySpuDescById(@PathVariable("spuId") Long spuId);

//    查询分组及其组下的值
    @GetMapping("pms/attrgroup/item/group/{cid}/{spuId}")
    public Resp<List<GroupVO>> queryGroupVOByCid(@PathVariable("cid")Long cid,@PathVariable("spuId")Long spuId);

}

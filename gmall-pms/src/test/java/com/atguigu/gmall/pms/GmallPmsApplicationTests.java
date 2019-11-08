package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.dao.BrandDao;
import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.service.BrandService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class GmallPmsApplicationTests {

    @Autowired
    private BrandDao brandDao;
    @Autowired
    private BrandService brandService;

    @Test
    void contextLoads() {
    }

    @Test
    public void test(){
//        BrandEntity brandEntity = new BrandEntity();
//        brandEntity.setDescript("尚硅谷真好");
//        brandEntity.setFirstLetter("S");
//        brandEntity.setLogo("www.baidu.com/log.gif");
//        brandEntity.setName("尚硅谷");
//        brandEntity.setShowStatus(0);
//        brandEntity.setSort(1);
//        this.brandDao.insert(brandEntity);
//        Map<String,Object> map = new HashMap<>();
//        map.put("name","fds发多少");
//        this.brandDao.deleteByMap(map);
//        System.out.println(this.brandDao.selectList(new QueryWrapper<BrandEntity>().eq("name", "尚硅谷")));

        //分页查询测试
//        IPage<BrandEntity> page = this.brandDao.selectPage(new Page<BrandEntity>(2, 2), null);
//        System.out.println(page.getRecords());
//        System.out.println(page.getTotal());
//        System.out.println(page.getPages());
        IPage<BrandEntity> page = this.brandService.page(new Page<BrandEntity>(2, 2), new QueryWrapper<BrandEntity>());
        System.out.println(page.getRecords());
        System.out.println(page.getTotal());
        System.out.println(page.getPages());
    }

}

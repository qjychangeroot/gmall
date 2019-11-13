package com.atguigu.gmall.index.service.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.index.annotation.GmallCache;
import com.atguigu.gmall.index.config.GmallJedisConfig;
import com.atguigu.gmall.index.feign.GmallPmsClient;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.vo.CategoryVO;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private GmallPmsClient gmallPmsClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedissonClient redissonClient;

    private static final String KEY_PREFIX = "index:category";

    private static final String TIMEOUT = "30000";


    @Override
    public List<CategoryEntity> queryLevel1Category() {

        Resp<List<CategoryEntity>> resp = this.gmallPmsClient.queryCategories(1, null);
        List<CategoryEntity> respData = resp.getData();
        return respData;
    }

    @GmallCache(prefix = KEY_PREFIX ,timeout = 300000L , random = 50000L)
    public List<CategoryVO> queryCategoryVo(Long pid) {
        // 1. 查询缓存 ，缓存中有的话直接返回
//        String cache = this.redisTemplate.opsForValue().get(KEY_PREFIX + pid);
//        if (StringUtils.isNotEmpty(cache)){
//            return JSON.parseArray(cache,CategoryVO.class);
//        }
        // 2. 如果缓存中没有，查询数据库
        Resp<List<CategoryVO>> listResp = this.gmallPmsClient.queryCategoryWithSub(pid);
        List<CategoryVO> categoryVOS = listResp.getData();
        // 3. 查询完成后，放入缓存

        //加上时间解决雪崩问题    5+(int)(Math.random()*5),TimeUnit.DAYS
        //空值加进去解决穿透问题（redis内没有缓存，请求后直接穿过redis为穿透）
//        this.redisTemplate.opsForValue().set(KEY_PREFIX+pid,JSON.toJSONString(categoryVOS),5+(int)(Math.random()*5),TimeUnit.DAYS );
        return categoryVOS;
    }

//    public String testLock1() {
//        // 所有请求，竞争锁
//        String uuid = UUID.randomUUID().toString();
//        Boolean lock = this.redisTemplate.opsForValue().setIfAbsent("lock", uuid, 10,TimeUnit.SECONDS);
//        // 获取到锁执行业务逻辑
//        if (lock){
//            String numString = this.redisTemplate.opsForValue().get("num");
//            if (StringUtils.isEmpty(numString)){
//                return null;
//            }
//            int num = Integer.parseInt(numString);
//            this.redisTemplate.opsForValue().set("num",String.valueOf(++num));
//
//            //释放锁
//            Jedis jedis = null;
//            try {
//                jedis = this.jedisPool.getResource();
//                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
//                jedis.eval(script,Arrays.asList("lock"),Arrays.asList(uuid));
//            } finally {
//                if (jedis != null){
//                    jedis.close();
//                }
//            }
////            this.redisTemplate.execute(new DefaultRedisScript<>(script), Arrays.asList("lock"),uuid);
////            String lock1 = this.redisTemplate.opsForValue().get("lock");
////            if (StringUtils.equals(uuid,lock1)){
////                this.redisTemplate.delete("lock");
////            }
//        }else {
//            // 没有获取到锁的请求进行重试
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                testLock();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return "已经增加成功";
//    }


    @Override
    public String testLock() {
        RLock lock = this.redissonClient.getLock("lock");
        lock.lock();

        // 获取到锁执行业务逻辑
        String numString = this.redisTemplate.opsForValue().get("num");
        if (StringUtils.isEmpty(numString)){
            return null;
        }
        int num = Integer.parseInt(numString);
        this.redisTemplate.opsForValue().set("num",String.valueOf(++num));

        lock.unlock();
        return "已经增加成功";
    }


    /**
     * 以下是copy老师github上的，时间如果允许可以自行练习一遍
     * @return
     */

    public String testRead() {
        RReadWriteLock readWriteLock = this.redissonClient.getReadWriteLock("readWriteLock");
        readWriteLock.readLock().lock(10l, TimeUnit.SECONDS);

        String msg = this.redisTemplate.opsForValue().get("msg");

//        readWriteLock.readLock().unlock();
        return msg;
    }

    public String testWrite() {
        RReadWriteLock readWriteLock = this.redissonClient.getReadWriteLock("readWriteLock");
        readWriteLock.writeLock().lock(10l, TimeUnit.SECONDS);

        String msg = UUID.randomUUID().toString();
        this.redisTemplate.opsForValue().set("msg", msg);

//        readWriteLock.writeLock().unlock();
        return "数据写入成功。。 " + msg;
    }

    public String latch() throws InterruptedException {

        RCountDownLatch latchDown = this.redissonClient.getCountDownLatch("latchDown");

//        String countString = this.redisTemplate.opsForValue().get("count");
//        int count = Integer.parseInt(countString);
        latchDown.trySetCount(5);

        latchDown.await();
        return "班长锁门。。。。。";
    }

    public String out() {
        RCountDownLatch latchDown = this.redissonClient.getCountDownLatch("latchDown");

//        String countString = this.redisTemplate.opsForValue().get("count");
//        int count = Integer.parseInt(countString);
//        this.redisTemplate.opsForValue().set("count", String.valueOf(--count));

        latchDown.countDown();
        return "出来了一个人。。。。";
    }
}


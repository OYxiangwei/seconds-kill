package com.oy.secondskill.service.impl;

import com.oy.secondskill.common.dynamicquery.DynamicQuery;
import com.oy.secondskill.common.entity.Result;
import com.oy.secondskill.common.entity.SuccessKilled;
import com.oy.secondskill.common.enums.SeckillStatEnum;
import com.oy.secondskill.distributedlock.redis.RedissLockUtil;
import com.oy.secondskill.distributedlock.zookeeper.ZkLockUtil;
import com.oy.secondskill.service.ISeckillDistributedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SeckillDistributedServiceImpl implements ISeckillDistributedService {
    @Autowired
    private DynamicQuery dynamicQuery;

    @Override
    @Transactional
    public Result startSeckilRedisLock(long seckillId, long userId) {
        boolean res = false;
        try {
            res = RedissLockUtil.tryLock(seckillId+"", TimeUnit.SECONDS, 3, 20);
            if(res){
                String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
                Object object =  dynamicQuery.nativeQueryObject(nativeSql, new Object[]{seckillId});
                Long number =  ((Number) object).longValue();
                if(number>0){
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState((short)0);
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    dynamicQuery.save(killed);
                    nativeSql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=? AND number>0";
                    dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[]{seckillId});
                }else{
                    return Result.error(SeckillStatEnum.END);
                }
            }else{
                return Result.error(SeckillStatEnum.MUCH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(res){//释放锁
                RedissLockUtil.unlock(seckillId+"");
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }
    @Override
    @Transactional
    public Result startSeckilZksLock(long seckillId, long userId) {
        boolean res=false;
        try {
            //基于redis分布式锁 基本就是上面这个解释 但是 使用zk分布式锁 使用本地zk服务 并发到10000+还是没有问题，谁的锅？
            res = ZkLockUtil.acquire(3,TimeUnit.SECONDS);
            if(res){
                String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
                Object object =  dynamicQuery.nativeQueryObject(nativeSql, new Object[]{seckillId});
                Long number =  ((Number) object).longValue();
                if(number>0){
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState((short)0);
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    dynamicQuery.save(killed);
                    nativeSql = "UPDATE seckill  SET number=number-1 WHERE seckill_id=? AND number>0";
                    dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[]{seckillId});
                }else{
                    return Result.error(SeckillStatEnum.END);
                }
            }else{
                return Result.error(SeckillStatEnum.MUCH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(res){//释放锁
                ZkLockUtil.release();
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }

    @Override
    @Transactional
    public Result startSeckilLock(long seckillId, long userId, long number) {
        boolean res=false;
        try {
            //尝试获取锁，最多等待3秒，上锁以后10秒自动解锁（实际项目中推荐这种，以防出现死锁）
            res = RedissLockUtil.tryLock(seckillId+"", TimeUnit.SECONDS, 3, 10);
            if(res){
                String nativeSql = "SELECT number FROM seckill WHERE seckill_id=?";
                Object object =  dynamicQuery.nativeQueryObject(nativeSql, new Object[]{seckillId});
                Long count =  ((Number) object).longValue();
                if(count>=number){
                    SuccessKilled killed = new SuccessKilled();
                    killed.setSeckillId(seckillId);
                    killed.setUserId(userId);
                    killed.setState((short)0);
                    killed.setCreateTime(new Timestamp(new Date().getTime()));
                    dynamicQuery.save(killed);
                    nativeSql = "UPDATE seckill  SET number=number-? WHERE seckill_id=? AND number>0";
                    dynamicQuery.nativeExecuteUpdate(nativeSql, new Object[]{number,seckillId});
                }else{
                    return Result.error(SeckillStatEnum.END);
                }
            }else{
                return Result.error(SeckillStatEnum.MUCH);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(res){//释放锁
                RedissLockUtil.unlock(seckillId+"");
            }
        }
        return Result.ok(SeckillStatEnum.SUCCESS);
    }
        }



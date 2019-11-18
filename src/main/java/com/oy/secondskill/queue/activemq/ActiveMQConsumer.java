package com.oy.secondskill.queue.activemq;

import com.oy.secondskill.common.entity.Result;
import com.oy.secondskill.common.enums.SeckillStatEnum;
import com.oy.secondskill.common.redis.RedisUtil;
import com.oy.secondskill.common.webSocket.WebSocketServer;
import com.oy.secondskill.service.ISeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class ActiveMQConsumer {
    @Autowired
    private ISeckillService seckillService;
    @Autowired
    private RedisUtil redisUtil;

    // 使用JmsListener配置消费者监听的队列，其中text是接收到的消息
    @JmsListener(destination = "seckill.queue")
    public void receiveQueue(String message) {
        //收到通道的消息之后执行秒杀操作(超卖)
        String[] array = message.split(";");
        Result result = seckillService.startSeckilDBPCC_TWO(Long.parseLong(array[0]), Long.parseLong(array[1]));
        if(result.equals(Result.ok(SeckillStatEnum.SUCCESS))){
            WebSocketServer.sendInfo(array[0].toString(), "秒杀成功");//推送给前台
        }else{
            WebSocketServer.sendInfo(array[0].toString(), "秒杀失败");//推送给前台
            redisUtil.cacheValue(array[0], "ok");//秒杀结束
        }
    }
}

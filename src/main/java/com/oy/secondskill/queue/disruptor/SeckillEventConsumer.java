package com.oy.secondskill.queue.disruptor;


import com.lmax.disruptor.EventHandler;
import com.oy.secondskill.common.config.SpringUtil;
import com.oy.secondskill.service.ISeckillService;

public class SeckillEventConsumer implements EventHandler<SeckillEvent> {
    private ISeckillService seckillService = (ISeckillService) SpringUtil.getBean("seckillService");

    public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) throws Exception {
        seckillService.startSeckil(seckillEvent.getSeckillId(), seckillEvent.getUserId());
    }
}

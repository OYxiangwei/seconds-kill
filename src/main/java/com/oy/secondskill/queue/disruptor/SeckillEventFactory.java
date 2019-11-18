package com.oy.secondskill.queue.disruptor;

import com.lmax.disruptor.EventFactory;

public class SeckillEventFactory implements EventFactory {
    public SeckillEvent newInstance() {
        return new SeckillEvent();
    }
}

package com.vip.config.loop.lmax;

import com.lmax.disruptor.EventFactory;


/**
 * 默认的事件工厂
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/05 09:08
 * @since 1.0.0
 */
public class DefaultEventFactory implements EventFactory<EventData> {
    @Override
    public EventData newInstance() {
        return new EventData();
    }
}
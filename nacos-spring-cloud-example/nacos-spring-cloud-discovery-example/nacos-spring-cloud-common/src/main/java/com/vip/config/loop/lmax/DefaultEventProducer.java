package com.vip.config.loop.lmax;


import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.vip.config.loop.ProducerHandler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认的事件生成者
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/05 09:08
 * @since 1.0.0
 */

public class DefaultEventProducer<T> {


    ProducerHandler<T> producerHandler;

    private final RingBuffer<EventData<T>> ringBuffer;
    public DefaultEventProducer(RingBuffer<EventData<T>> ringBuffer, ProducerHandler<T> producerHandler) {
        this.ringBuffer = ringBuffer;
        this.producerHandler = producerHandler;
    }

    /**
     * 生产数据
     */
    private final EventTranslatorOneArg<EventData<T>,T> tranlator = (tEventData, sequence, t) -> tEventData.setData(t);

    /**
     * onData用来发布事件，每调用一次就发布一次事件，它的参数会通过事件传递给消费者
     */
    public void onData(T data) {
        ringBuffer.publishEvent(tranlator, data);

    }

}

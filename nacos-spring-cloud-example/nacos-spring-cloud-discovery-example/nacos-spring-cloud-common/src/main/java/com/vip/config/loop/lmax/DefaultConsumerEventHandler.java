package com.vip.config.loop.lmax;

import com.lmax.disruptor.WorkHandler;
import com.vip.config.loop.LoopEngine;
import com.vip.config.loop.ProducerHandler;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 批次事件处理,计算init批次和是否满足推送pms要求,多线程并发
 * <p>
 * 推送给pms数据：0默认状态 ,-1:无需推送，-2：计算异常，1：pms铺货销量，2：pms退货订单
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/05 09:08
 * @since 1.0.0
 */
@Log4j2
@AllArgsConstructor
public class DefaultConsumerEventHandler<T> implements WorkHandler<EventData> {
    private ProducerHandler<T> producerHandler;
    private AtomicLong count;
    private LoopEngine loopEngine;
    @Override
    public void onEvent(EventData eventData) throws Exception {
         if (loopEngine.isRun()) {
             boolean result = producerHandler.worker((T) eventData.getData());
             if (result){
                 count.incrementAndGet();
                 producerHandler.progress(count.intValue());
             }else {
                 loopEngine.setRun(false);
             }

         }
    }
}

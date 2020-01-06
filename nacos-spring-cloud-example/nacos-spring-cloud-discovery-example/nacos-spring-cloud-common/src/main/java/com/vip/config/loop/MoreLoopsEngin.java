package com.vip.config.loop;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.vip.config.loop.lmax.DefaultEventFactory;
import com.vip.config.loop.lmax.DefaultEventProducer;
import com.vip.config.loop.lmax.EventData;
import com.vip.config.loop.lmax.MaxThreadFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/03 17:20
 * @since 1.0.0
 */
public class MoreLoopsEngin<T> extends LoopEngine<T> {

    protected AtomicLong pageStart ;

    @Override
    public boolean loop() {
        long startTime = System.currentTimeMillis();
        pageStart =  new AtomicLong(start);
        CountDownLatch countDownLatch=new CountDownLatch(producerNum);
        try {
            for (int i = 0; i < producerNum; i++) {
//                DefaultEventProducer  tProducer = new DefaultEventProducer(disruptor.getRingBuffer(), producerHandler);
                new Thread(() -> {
                    try {
                        while (isRun) {
                            if (end == pageStart.intValue()) {
                                producerHandler.end(count.intValue(), false, true);
                                return;
                            }
                            List<T> result = producerHandler.loop(pageStart.getAndIncrement());
                            if (result == null || result.size() == 0) {
                                producerHandler.end(count.intValue(), false, true);
                                return;
                            }
                            //遍历每一个data
                            for (T entity : result) {
                                //运行时间过长了
                                if (isTimeouts(startTime)) {
                                    producerHandler.end(count.intValue(), true, false);
                                    return ;
                                }
                                producer.onData(entity);
                                count.incrementAndGet();
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        producerHandler.fail(e,null);
                    }finally {
                        countDownLatch.countDown();
                    }
                },name+"-Thread-"+i).start();
            }
            countDownLatch.await();
        } catch (Exception e) {
            producerHandler.fail(e,null);
            return false;
        } finally {
            System.out.println("结束开始");
            disruptor.shutdown();
            System.out.println("结束结束");
            clear();
            //准备锁
            producerHandler.finish(count.intValue(),consumerCount.intValue());
        }
        return true;

    }

    @Override
    protected void createDisruptor(DefaultEventFactory peopleEventFactory, int buffSize) {
        disruptor = new Disruptor<EventData>(peopleEventFactory, buffSize, new MaxThreadFactory(),
                ProducerType.MULTI, new BlockingWaitStrategy());
    }
}

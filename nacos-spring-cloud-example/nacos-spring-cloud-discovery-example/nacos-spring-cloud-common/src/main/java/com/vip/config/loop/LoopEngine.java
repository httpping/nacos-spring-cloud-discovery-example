package com.vip.config.loop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.vip.config.loop.lmax.*;
import com.vip.config.loop.monitor.LoopMonitorService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/05 09:08
 * @since 1.0.0
 */
@Getter
@Setter
@Log4j2
public class LoopEngine<T> {

    protected int ringBatchSize = 16;
    @JsonIgnore
    protected Disruptor<EventData> disruptor;
    @JsonIgnore
    protected DefaultEventProducer producer;
    @JsonIgnore
    protected ProducerHandler<T> producerHandler;
    @JsonIgnore
    protected ExceptionHandler exceptionHandler;

    protected AtomicLong count = new AtomicLong(0);
    protected long timeout = -1;
    protected int maxWork = 1;
    protected int start;
    protected int end = -1;
    protected AtomicLong consumerCount = new AtomicLong(0);

    protected String name;
    protected String id ;

    protected boolean isRun = true;
    @JsonIgnore
    private LoopMonitorService loopMonitorService;

    /**
     * 生产者数量
     */
    protected int producerNum =1;


    protected long startTime = System.currentTimeMillis();


    public void initEnv() {
        //事件工厂用来创建事件
        DefaultEventFactory peopleEventFactory = new DefaultEventFactory();
        //指定Ring Buffer大小，2的倍数
        int buffSize = ringBatchSize;

        createDisruptor(peopleEventFactory, buffSize);
        //从Disruptor获取RingBuffer，用来发布
        RingBuffer<EventData> ringBuffer = disruptor.getRingBuffer();

        WorkHandler[] workHandlers = new WorkHandler[maxWork];
        for (int i = 0; i < maxWork; i++) {
            workHandlers[i] = new DefaultConsumerEventHandler(producerHandler,consumerCount,this);
        }
        disruptor.handleEventsWithWorkerPool(workHandlers);
        if (exceptionHandler != null) {
            disruptor.setDefaultExceptionHandler(exceptionHandler);
        }else {
            disruptor.setDefaultExceptionHandler(new ExceptionHandler<EventData>() {
                @Override
                public void handleEventException(Throwable throwable, long l, EventData eventData) {
                    isRun = producerHandler.fail(throwable, (T) eventData.getData());
                }

                @Override
                public void handleOnStartException(Throwable throwable) {

                }

                @Override
                public void handleOnShutdownException(Throwable throwable) {

                }
            });
        }
        disruptor.start();
        producer = new DefaultEventProducer(disruptor.getRingBuffer(), producerHandler);

    }

    protected void createDisruptor(DefaultEventFactory peopleEventFactory, int buffSize) {
        disruptor = new Disruptor<EventData>(peopleEventFactory, buffSize, new MaxThreadFactory(this),
                ProducerType.SINGLE, new BlockingWaitStrategy());
    }

    public boolean loop() {
        long startTime = System.currentTimeMillis();
        int page = start;
        try {
            while (isRun) {

                if (end == page) {
                    producerHandler.end(count.longValue(), false, true);
                    return true;
                }

                List<T> result = producerHandler.loop(page);
                page++;

                if (result == null || result.size() == 0) {
                    producerHandler.end(count.longValue(), false, true);
                    return true;
                }
                //遍历每一个对账单
                for (T entity : result) {
                    //运行时间过长了
                    if (isTimeouts(startTime)) {
                        producerHandler.end(count.longValue(), true, false);
                        return true;
                    }
                    producer.onData(entity);
                    count.incrementAndGet();
                }

            }
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

    protected boolean isTimeouts(long startTime) {
        return System.currentTimeMillis() - startTime > timeout && timeout > 0;
    }

    /**
     * 结束运行
     * @return
     */
    public boolean shutdown(){
        isRun =false;
        return true;
    }


    public boolean register(){

        return true;
    }
    public boolean clear(){
        if (loopMonitorService!=null){
            loopMonitorService.remove(this);
        }
        return true;
    }

}

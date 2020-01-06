package com.vip.config.loop;

import com.lmax.disruptor.ExceptionHandler;
import com.vip.config.loop.monitor.LoopMonitorService;

import java.util.UUID;

/**
 *
 * @author tanping
 */
public class LoopEngineBuilder {

    /**
     * 工作handler
     */
    private ProducerHandler producerHandler;

    /**
     * 消费者线程数量
     */
    private int maxWork = 1;
    /**
     * 队列大小
     */
    private int ringBatchSize;
    /**
     * 异常操作自定义
     */
    private ExceptionHandler exceptionHandler;


    /**
     * 超时时间设置
     */
    private long timeout = -1;

    /**
     * 开始页数
     */
    private int start;
    /**
     * 结束页数 -1 数据为 null 结束
     */
    private int end = -1;

    /**
     * 引擎 name
     */
    private String name;
    /**
     * 生产者数量
     */
    private int producerNum =1;


    private LoopMonitorService loopMonitorService;

    public static LoopEngineBuilder newBuilder() {
        return new LoopEngineBuilder();
    }

    public LoopEngineBuilder setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public LoopEngineBuilder setStart(int start) {
        this.start = start;
        return this;
    }

    public LoopEngineBuilder setEnd(int end) {
        this.end = end;
        return this;
    }

    public LoopEngineBuilder setProducerHandler(ProducerHandler producerHandler) {
        this.producerHandler = producerHandler;
        return this;
    }

    public LoopEngineBuilder setMaxWork(int maxWork) {
        this.maxWork = maxWork;
        return this;
    }

    public LoopEngineBuilder setLoopMonitorService(LoopMonitorService loopMonitorService) {
        this.loopMonitorService = loopMonitorService;
        return this;
    }

    public LoopEngineBuilder setRingBatchSize(int ringBatchSize) {
        this.ringBatchSize = ringBatchSize;
        return this;
    }

    public LoopEngineBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public LoopEngineBuilder setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    public LoopEngineBuilder setProducerNum(int producerNum) {
        this.producerNum = producerNum;
        return this;
    }

    public LoopEngine build() {


        LoopEngine loopEngine = null;
        if (producerNum == 1){
            loopEngine = new LoopEngine();
        }else {
            loopEngine = new MoreLoopsEngin();
            loopEngine.setProducerNum(producerNum);
        }

        if (maxWork != 0) {
            loopEngine.setMaxWork(maxWork);
        }
        if (ringBatchSize != 0) {
            loopEngine.setRingBatchSize(ringBatchSize);
        }
        if (start != 0) {
            loopEngine.setStart(start);
        }
        if (end != 0) {
            loopEngine.setEnd(end);
        }
        if (timeout != 0) {
            loopEngine.setTimeout(timeout);
        }
        if (exceptionHandler != null) {
            loopEngine.setExceptionHandler(exceptionHandler);
        }
        if (name != null) {
            loopEngine.setName(name);
        }else {
            throw new IllegalArgumentException("name is null");
        }

        if (loopMonitorService !=null){
            loopEngine.setLoopMonitorService(loopMonitorService);
        }

        if (producerHandler != null) {
            loopEngine.setProducerHandler(producerHandler);
        } else {
            return null;
        }
        try {
            if (loopMonitorService!=null){
                loopMonitorService.register(loopEngine);
            }
            loopEngine.setId(UUID.randomUUID().toString());
            loopEngine.initEnv();
            loopEngine.loop();
        }finally {

        }

        return loopEngine;
    }

}
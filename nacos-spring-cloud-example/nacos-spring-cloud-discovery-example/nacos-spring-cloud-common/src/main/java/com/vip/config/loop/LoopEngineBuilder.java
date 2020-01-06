package com.vip.config.loop;

import com.lmax.disruptor.ExceptionHandler;

/**
 *
 */
public class LoopEngineBuilder {
    private ProducerHandler producerHandler;
    private int maxWork = 1;
    private int ringBatchSize;
    private ExceptionHandler exceptionHandler;
    private long timeout = -1;
    private int start;
    private int end = -1;
    private String name;
    /**
     * 生产者数量
     */
    private int producerNum =1;

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
        }
        if (producerHandler != null) {
            loopEngine.setProducerHandler(producerHandler);
        } else {
            return null;
        }
        loopEngine.initEnv();
        loopEngine.loop();
        return loopEngine;
    }

}
package com.vip.config.loop;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 测试引擎
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/03 14:59
 * @since 1.0.0
 */
@Log4j2
public class TestLmax {

    public static void main(String[] args) {

        ConcurrentHashMap<Long, Long> pageMap = new ConcurrentHashMap<>();
        ConcurrentHashMap<Long, Long> resultMap = new ConcurrentHashMap<>();
        AtomicLong dataCount = new AtomicLong();


        LoopEngineBuilder.newBuilder().setName("ABC").setEnd(100000).setMaxWork(10).setProducerNum(10).setRingBatchSize(128).setProducerHandler(new ProducerHandler<Long>() {
            int count = 0;
            @Override
            public List loop(long number) {

                if (pageMap.get(number) != null) {
                    throw new RuntimeException(number + "页数"+"==" + Thread.currentThread());
                }
                pageMap.put(number,number);
                if (pageMap.size() >= 10000){
                    pageMap.clear();
                }
                ArrayList<Long> list = new ArrayList<Long>();
                for (int i = 0; i < 10; i++) {
                    list.add(dataCount.getAndIncrement());
                }
                return list;
            }

            @Override
            public boolean worker(Long data) {
                if (data == 12) {
//                    throw new RuntimeException();
                }
                if (resultMap.get(data) != null) {
                    throw new RuntimeException(data + "结果");
                }
                if (resultMap.size() >= 10000){
                    resultMap.clear();
                }
                resultMap.put(data,data);
                System.out.println(Thread.currentThread() + ": " + Thread.currentThread().getId() + " : " + data);
                return true;
            }

            @Override
            public boolean finish(long count, long consumerCount) {
                log.info("finish {} {}", count, consumerCount);
                System.out.println("finish {} {}" + count + "," + consumerCount);
                return false;
            }

        }).build();


    }
}

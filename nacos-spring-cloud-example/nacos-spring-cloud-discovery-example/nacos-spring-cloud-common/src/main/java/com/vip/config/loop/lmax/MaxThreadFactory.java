package com.vip.config.loop.lmax;

import com.vip.config.loop.LoopEngine;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ThreadFactory;

/**
 *  线程工程
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/05 09:08
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public class MaxThreadFactory implements ThreadFactory {
    private LoopEngine loopEngine;
    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(loopEngine.getName()+"-consumer-loop-"+thread.getName());
        return thread;
    }
}

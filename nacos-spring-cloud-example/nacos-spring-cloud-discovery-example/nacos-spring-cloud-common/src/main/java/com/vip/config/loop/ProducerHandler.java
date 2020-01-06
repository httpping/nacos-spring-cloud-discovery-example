package com.vip.config.loop;

import java.util.List;

/**
 * TODO
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2019/12/18 10:29
 * @since 1.0.0
 */
public interface ProducerHandler<T> {

    /**
     * 开始
     * @return
     */
    default boolean start() {
        return true;
    }

    /**
     * 失败 true 继续生产， false停止生产
     * @return
     */

    default boolean fail(Throwable throwable,T data){
        throwable.printStackTrace();
        return false;
    }
    /**
     * 完成
     * @param count
     * @return
     */
    default boolean finish(long count,long consumerCount){
        return true;
    }

    /**
     * 成功结束，是否超时
     * @return
     */
    default boolean end(long count, boolean isTimeout, boolean isSuccess){
        return true;
    }

    /**
     * 循环取值，为 empt结束
     * @param number
     * @return
     */
    List<T> loop(long number);


    /**
     * 工作
     * @param t
     * @return
     */
    boolean worker(T t);

    /**
     * 跟进进度
     * @param count
     */
    default void progress(long count){

    };
}

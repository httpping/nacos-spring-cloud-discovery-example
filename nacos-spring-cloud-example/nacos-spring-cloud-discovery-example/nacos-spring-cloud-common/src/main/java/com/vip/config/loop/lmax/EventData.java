package com.vip.config.loop.lmax;

import lombok.Data;

/**
 * data 存放处理数据
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/05 09:08
 * @since 1.0.0
 */
@Data
public  class EventData<T> {

    T data;

    public void reset(){
        data = null;
    }

}
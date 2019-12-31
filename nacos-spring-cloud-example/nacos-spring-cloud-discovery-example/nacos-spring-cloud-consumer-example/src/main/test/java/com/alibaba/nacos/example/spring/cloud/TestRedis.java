package com.alibaba.nacos.example.spring.cloud;

import com.alibaba.nacos.example.spring.cloud.config.RedisUtil;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2019/12/31 12:17
 * @since 1.0.0
 */
@Log4j2
public class TestRedis extends BaseTest {

    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testRedis1(){

        redisUtil.set("test",1);

        Integer a = (Integer) redisUtil.get("test");

        log.info("aaa{}",a);

        UserTest userTest = new UserTest();
        userTest.setAge(12);
        userTest.setName("tax");

        redisUtil.set("tax",userTest);

        userTest = (UserTest) redisUtil.get("tax");
        log.info("tax {}",userTest);

    }
}

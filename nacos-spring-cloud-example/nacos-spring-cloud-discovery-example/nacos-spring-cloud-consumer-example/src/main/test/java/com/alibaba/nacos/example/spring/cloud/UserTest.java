package com.alibaba.nacos.example.spring.cloud;

import lombok.Data;

import java.io.Serializable;

/**
 * TODO
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2019/12/31 14:10
 * @since 1.0.0
 */
@Data
public class UserTest implements Serializable {
    String name ;
    Integer age ;
}

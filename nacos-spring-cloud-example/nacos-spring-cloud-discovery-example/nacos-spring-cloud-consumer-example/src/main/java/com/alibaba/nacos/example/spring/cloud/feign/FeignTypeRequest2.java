package com.alibaba.nacos.example.spring.cloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * TODO
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2019/09/23 12:05
 * @since 1.0.0
 */
@FeignClient(name = "service-provider2")
public interface FeignTypeRequest2 {

    @GetMapping("/echo/{str}")
    String echo(@PathVariable(value = "str") String str);

}

package com.alibaba.nacos.example.spring.cloud;

import com.alibaba.nacos.example.spring.cloud.feign.FeignTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author xiaojing
 */
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class NacosConsumerApplication {

    @Autowired
    FeignTypeRequest feignTest;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class, args);
    }

    @RestController
    public class TestController {

        private final RestTemplate restTemplate;

        @Autowired
        public TestController(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

        @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
        public String echo(@PathVariable String str) {
            try {
                Thread.sleep((long) (Math.random()*2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return feignTest.echo(str);
//            return restTemplate.getForObject("http://service-provider/echo/" + str, String.class);
        }

        @RequestMapping(value = "/echo2/{str}", method = RequestMethod.GET)
        public String echo2(@PathVariable String str) {
//            return feignTest.echo(str);
            return restTemplate.getForObject("http://service-provider/echo/" + str, String.class);
        }
    }
}

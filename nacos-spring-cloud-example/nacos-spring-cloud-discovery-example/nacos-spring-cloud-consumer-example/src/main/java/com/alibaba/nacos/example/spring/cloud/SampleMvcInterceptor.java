package com.alibaba.nacos.example.spring.cloud;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//请求拦截器
@Component
public class SampleMvcInterceptor extends HandlerInterceptorAdapter {

    private static final Counter COUNTER = Counter.builder("http_request")
            .tag("HttpCount", "url")
            .description("Http请求统计")
            .baseUnit("count")
            .register(Metrics.globalRegistry);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) throws Exception {
        COUNTER.increment();
    }
}

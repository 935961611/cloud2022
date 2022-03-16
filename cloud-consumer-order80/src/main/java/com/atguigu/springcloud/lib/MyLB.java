package com.atguigu.springcloud.lib;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;


import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
@Component
public class MyLB implements LoadBalancer {
    private AtomicInteger atomicInteger = new AtomicInteger(0);


    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = atomicInteger.get();
            next = current >= 2147483647 ? 0: current + 1;
        } while (!atomicInteger.compareAndSet(current,next));

        log.info(String.format("第%d次访问",next));
        return next;
    };

    @Override
    public ServiceInstance instance(List<ServiceInstance> serviceInstances) {

       int index = getAndIncrement() % serviceInstances.size();


       return serviceInstances.get(index);
    }


}

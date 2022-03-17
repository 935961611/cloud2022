package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.CommonResult;
import com.atguigu.springcloud.entities.Payment;
import com.atguigu.springcloud.service.PaymentService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import sun.plugin.com.DispatchClient;

import javax.annotation.Resources;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${server.port}")
    private String serverPort;



    @PostMapping("/payment/create")
    public CommonResult create(@RequestBody Payment payment){

        int result = paymentService.create(payment);

        log.info("插入结果："+result);
        if(result > 0){
            return new CommonResult(200,"插入数据库成功，serverPort:"+serverPort,result);
        }else {

            return new CommonResult(444,"插入数据库失败！");
        }
    }

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id") Long id){
        Payment payment = paymentService.getPaymentById(id);
        log.info("查询成功！");

        if(payment != null){
            return new CommonResult(200,"查询成功! serverPort: "+serverPort,payment);
        }else {
            return new CommonResult(444,"插入失败！"+id);
        }

    }

    @GetMapping("/payment/discovery")
    public Object discover(){

        List<String> services = discoveryClient.getServices();

        for (String s :services){
            log.info("********service*****"+s);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for(ServiceInstance s : instances){
            log.info("******instance***port:"+s.getPort()+"\t"+s.getServiceId());
        }
        return this.discoveryClient;
    }

    @GetMapping(value = "/payment/lb")
    public String getPaymentLB() {
        return serverPort;//返回服务接口
    }

    @GetMapping(value = "/payment/feign/timeout")
    public String paymentFeignTimeout()
    {
        // 业务逻辑处理正确，但是需要耗费3秒钟
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return serverPort;
    }
    @GetMapping("/payment/zipkin")
    public String paymentZipkin() {
        return "hi ,i'am paymentzipkin server fall back，welcome to here, O(∩_∩)O哈哈~";
    }
}

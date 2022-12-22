package com.finist.microservices2022.gatewaylibraryservice.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;


//@Component
public class RequestRepeater implements Runnable{

    public boolean isRunning;

    private final BlockingQueue<RequestDelayObject<?,?>> delayQueue;

    private final RestTemplate restTemplate;

    public RequestRepeater(BlockingQueue<RequestDelayObject<?, ?>> delayQueue, RestTemplate restTemplate) {
        this.delayQueue = delayQueue;
        this.restTemplate = restTemplate;
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning){
            RequestDelayObject<?, ?> rdo = delayQueue.poll();
            if(rdo != null){
                try {
                    makeRequest(rdo);
                }
                catch (ResourceAccessException ex){
                    delayQueue.add(rdo);
                }
            }
        }
    }

    public ResponseEntity<?> makeRequest(RequestDelayObject<?,?> rdo) {
        if (rdo.httpMethod.equals("GET")) {
            ResponseEntity<?> respEntity = null;
            respEntity = rdo.restTemplate.getForEntity(rdo.requestUri, rdo.responseEntityClass);
            return respEntity;
        } else if (rdo.httpMethod.equals("POST")) {
            ResponseEntity<?> respEntity = null;
            respEntity = this.restTemplate.postForEntity(rdo.requestUri, rdo.requestBodyObject, rdo.responseEntityClass);
            return respEntity;
        }
        return null;
    }
}

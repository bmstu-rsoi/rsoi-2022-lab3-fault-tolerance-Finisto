package com.finist.microservices2022.gatewaylibraryservice.queue;

import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

//@Component
public class RequestDelayObject<T1, T2> implements Delayed {

    public final URI requestUri;

    public final T1 requestBodyObject;

    public final String httpMethod;

    public final Class<T2> responseEntityClass;
    public final RestTemplate restTemplate;

    private final long time;

    public RequestDelayObject(URI requestUri, T1 requestBodyObject, String httpMethod, Class<T2> responseEntityClass, RestTemplate restTemplate, long delayTime) {
        this.requestUri = requestUri;
        this.requestBodyObject = requestBodyObject;
        this.httpMethod = httpMethod;
        this.responseEntityClass = responseEntityClass;
        this.restTemplate = restTemplate;
        this.time = System.currentTimeMillis() + delayTime;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long diff = time - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (this.time < ((RequestDelayObject<?, ?>) o).time) {
            return -1;
        } else if (this.time > ((RequestDelayObject<?, ?>) o).time) {
            return 1;
        }
        return 0;
    }


}

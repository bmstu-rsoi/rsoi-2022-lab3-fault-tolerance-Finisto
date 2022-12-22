package com.finist.microservices2022.gatewaylibraryservice.queue;

import java.net.URI;
import java.util.UUID;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

//@Component
public class RequestDelayObject<T1, T2> implements Delayed {

    public final UUID requestUid;

    public final URI requestUri;

    public final T1 requestBodyObject;

    public final String httpMethod;

    public final Class<T2> responseEntityClass;

    private final long time;

    public RequestDelayObject(UUID requestUid, URI requestUri, T1 requestBodyObject, String httpMethod, Class<T2> responseEntityClass, long delayTime) {
        this.requestUid = requestUid;
        this.requestUri = requestUri;
        this.requestBodyObject = requestBodyObject;
        this.httpMethod = httpMethod;
        this.responseEntityClass = responseEntityClass;
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

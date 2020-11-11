package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.EndpointUrlFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

public class DelayTest extends ServerTestBase {


    @Test
    public void positiveDelayShouldDelay(){
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        PreparedHttpResponse preparedHttpResponse = new PreparedHttpResponse(new HttpResponseToSend(200, "Hey"), new EndpointUrlFilter("/superman"));
        preparedHttpResponse.delay = 200;
        RegisteredPreparedHttpResponses.getInstance().add(preparedHttpResponse);
        long startTime = System.currentTimeMillis();
        String response = getAndGetResponse("/superman").body;
        Assert.assertTrue(System.currentTimeMillis() - startTime > 198);
        Assert.assertTrue(response, "Hey".equals(response));

    }
}

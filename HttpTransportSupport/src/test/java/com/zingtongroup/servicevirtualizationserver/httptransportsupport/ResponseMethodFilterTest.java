package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.EndpointUrlFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.HttpMethodVerbFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ResponseMethodFilterTest extends ServerTestBase {
    @Test
    public void methodFilterShouldReturnResponseUponCorrectMethod() throws IOException, InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend responseToSend = new HttpResponseToSend(200, "Hello gorgeous");
        HttpResponseToSend postResponseToSend = new HttpResponseToSend(200, "Hello ugly");
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(responseToSend, new HttpMethodVerbFilter("GET"), new EndpointUrlFilter(("/festivities"))));
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(postResponseToSend, new HttpMethodVerbFilter("POST"), new EndpointUrlFilter(("/festivities"))));

        String postResponse = this.postAndGetResponse("{dummy}", "/festivities");
        Assert.assertFalse(postResponse.equals("Hello gorgeous"));
        Assert.assertTrue(postResponse, postResponse.equals("Hello ugly"));

        String response = getAndGetResponse("/festivities");
        Assert.assertFalse(response.equals("Hello ugly" ));
        Assert.assertTrue(response.equals("Hello gorgeous" ));
    }

}

package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.EndpointUrlFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class EndpointUrlFilterTest extends ServerTestBase {


    @Test
    public void correctEndpointShouldReturnResponse() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Hey"), new EndpointUrlFilter("/superman")));
        String response = getAndGetResponse("/superman");
        Assert.assertTrue(response, "Hey".equals(response));
    }

    @Test
    public void wrongEndpointShouldNotReturnResponse() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Hey"), new EndpointUrlFilter("/superman")));
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Nay"), new EndpointUrlFilter("/superman2")));
        String response = getAndGetResponse("/superman");
        System.out.println("Response '" + response + "'");
        Assert.assertTrue(response, response.equals("Hey" ));
    }

    @Test
    public void reservedEndpointsShouldNotOverrideDefaultBehavior() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Hey"), new EndpointUrlFilter("/admin")));
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Hey"), new EndpointUrlFilter("/api")));
        String response = getAndGetResponse("/admin");
        Assert.assertNotNull(response, response);
        Assert.assertFalse(response, response.equals("Hey"));
        response = getAndGetResponse("/api");
        Assert.assertFalse(response, response.length() == 0);
        Assert.assertNotNull(response, response);
        Assert.assertFalse(response, response.length() == 0);
        Assert.assertFalse(response, response.equals("Hey"));
    }
}

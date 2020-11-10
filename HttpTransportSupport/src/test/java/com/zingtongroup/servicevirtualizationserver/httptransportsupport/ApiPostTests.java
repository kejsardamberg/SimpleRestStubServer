package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ApiPostTests extends ServerTestBase{

    @Test
    public void receivingLargeStringAsBodyShouldWork() throws IOException, InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        StringBuilder longString = new StringBuilder();
        longString.append("<");
        while (longString.toString().length() < 10000){
            longString.append("1234567890");
        }
        longString.append(">");
        String postBody = "{\"httpResponse\":{\"headers\":[],\"body\":\"" + longString.toString() + "\",\"bodyType\":\"direct\",\"responseCode\":\"200\"},\"filters\":[{\"type\":\"EndpointUrlFilter\",\"field1\":\"Cadabra\"}]}\n";
        String responseBody = postAndGetResponse(postBody);
    }

    @Test
    public void postAnyThingUnParsableCreatesNextResponse() throws IOException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        postAndGetResponse("Hello nice one");
        String response = getAndGetResponse("/sdfasdg");
        Assert.assertTrue(response.equals("Hello nice one"));
    }

    @Test
    public void postingNewNextRegisteredResponseShouldReturnIt() throws IOException, InterruptedException {
        //POST new RegisteredResponse
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        String postBody = "{\"httpResponse\":{\"headers\":[],\"body\":\"Abra\",\"bodyType\":\"direct\",\"responseCode\":\"200\"},\"filters\":[{\"type\":\"EndpointUrlFilter\",\"field1\":\"Cadabra\"}]}\n";
        postAndGetResponse(postBody);

        String response = getAndGetResponse("/api");

        Assert.assertTrue(response, response.contains("Abra"));
        Assert.assertTrue(response, response.contains("Cadabra"));

    }

}

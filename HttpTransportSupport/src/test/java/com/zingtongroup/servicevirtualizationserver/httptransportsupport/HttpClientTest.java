package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.clientcomponents.HttpClient;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.clientcomponents.HttpClient2;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.clientcomponents.HttpResponseInternal;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpRequest;
import org.junit.Test;

import java.io.IOException;

public class HttpClientTest {

    @Test
    public void te() throws IOException, InterruptedException {
        HttpClient2 client = new HttpClient2();
        HttpRequest request = new HttpRequest();
        request.setUrl("http://damberg.one/crafts.html");
        request.setVerb("GET");
        HttpResponseInternal response = client.sendRequest(request);
        System.out.println(response.toString());
    }
}

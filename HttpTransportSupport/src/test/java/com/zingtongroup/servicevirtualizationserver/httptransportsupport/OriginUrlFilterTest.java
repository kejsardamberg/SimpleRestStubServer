package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.OriginUrlFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class OriginUrlFilterTest extends ServerTestBase {

    @Test
    public void originMatchShouldRespond() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new OriginUrlFilter("127.0.0.1:" + PORT)));
        String response = getAndGetResponse("/sdaaasdg").body;
        Assert.assertTrue(response.equals("Yes"));
    }

    @Test
    public void originNonMatchShouldNotRespond()  {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new OriginUrlFilter("134.124.124.111:9090")));
        HttpResponse response = getAndGetResponse("/sdaaasdg");
        Assert.assertTrue(response.responseCode == 404);
    }
}

package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.BodyContainsMatchFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BodyContainsMatchFilterTest extends ServerTestBase {

    @Test
    public void bodyContainsMatchShouldRespond() throws IOException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new BodyContainsMatchFilter("This")));
        String response = postAndGetResponse("Anything and This of course", "/sdaaasdg").body;
        Assert.assertTrue(response.equals("Yes"));
    }

    @Test(expected = FileNotFoundException.class)
    public void bodyDoNotContainShouldNotReturnResponse() throws IOException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new BodyContainsMatchFilter("This")));
        String response = postAndGetResponse("Anything but that of course", "/sdaaasdg").body;
        Assert.assertFalse(response.equals("Yes"));
    }
}

package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.BodyContainsMatchFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.BodyRegexMatchFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BodyRegexMatchFilterTest extends ServerTestBase {

    @Test
    public void bodyRegexMatchShouldRespond() throws IOException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new BodyRegexMatchFilter(".*This.*")));
        String response = postAndGetResponse("Anything and This of course", "/sdaaasdg");
        Assert.assertTrue(response.equals("Yes"));
    }

    @Test(expected = FileNotFoundException.class)
    public void bodyDoNotMatchRegexShouldNotReturnResponse() throws IOException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new BodyRegexMatchFilter(".*This.*")));
        String response = postAndGetResponse("Anything but that of course", "/sdaaasdg");
        Assert.assertFalse(response.equals("Yes"));
    }

}

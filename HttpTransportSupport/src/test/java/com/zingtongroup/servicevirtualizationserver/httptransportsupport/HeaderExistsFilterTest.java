package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.HeaderExistFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class HeaderExistsFilterTest extends ServerTestBase {

    @Test
    public void headerExistMatchShouldRespond() throws FileNotFoundException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new HeaderExistFilter("Content-Language")));
        String response = getAndGetResponse("/sdaaasdg").body;
        Assert.assertTrue(response.equals("Yes"));
    }

    @Test
    public void headerDoesNotExistShouldNotReturnResponse() {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new HeaderExistFilter("This")));
        HttpResponse response = getAndGetResponse("/sdaaasdg");
        Assert.assertTrue(response.responseCode == 404);
    }

}

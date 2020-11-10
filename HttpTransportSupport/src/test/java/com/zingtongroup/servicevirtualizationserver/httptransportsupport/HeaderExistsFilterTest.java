package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.BodyContainsMatchFilter;
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
        String response = getAndGetResponse("/sdaaasdg");
        Assert.assertTrue(response.equals("Yes"));
    }

    @Test
    public void headerDoesNotExistShouldNotReturnResponse() {
        Exception e = null;
        try{
            RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
            RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(new HttpResponseToSend(200, "Yes"), new HeaderExistFilter("This")));
            String response = getAndGetResponse("/sdaaasdg");
        }catch (Exception w){
            System.out.println(w.getMessage());
            e = w;
        }
        Assert.assertNotNull(e);
    }

}

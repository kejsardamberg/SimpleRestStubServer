package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.NextResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;

public class BodyFromFileTest extends ServerTestBase {

    @BeforeClass
    public static void copyFile() throws IOException {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("helloworldcontent.txt");
        File outFile = new File("helloworldcontent.txt");
        if(outFile.exists()) return;

        byte[] buffer = new byte[is.available()];
        is.read(buffer);

        OutputStream outStream = new FileOutputStream(outFile);
        outStream.write(buffer);
    }

    @Test
    public void responseBodyAsFileShouldBeSentIfRegistered() throws Exception {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend responseToSend = new HttpResponseToSend(200, null);
        responseToSend.bodyFilePath = "helloworldcontent.txt";
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(responseToSend, new NextResponse()));
        String response = getAndGetResponse("/hello").body;
        System.out.println("Out: '" + response + "'");
        Assert.assertTrue(response.equals("Hollow world"));
    }

    @Test
    public void responseBodyFromFileShouldBeSentIfPosted() throws IOException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        String postBody = "{\"httpResponse\":{\"headers\":[],\"bodyFilePath\":\"helloworldcontent.txt\",\"bodyType\":\"file\",\"responseCode\":\"200\"},\"filters\":[{\"type\":\"EndpointUrlFilter\",\"field1\":\"/hello\"}]}\n";
        postAndGetResponse(postBody);
        String response = getAndGetResponse("/hello").body;
        System.out.println("Out: '" + response + "'");
        Assert.assertTrue(response.equals("Hollow world"));
    }
}

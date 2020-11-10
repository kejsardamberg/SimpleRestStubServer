package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.NextResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NextResponseFilterTest extends ServerTestBase{

    @Test
    public void nextResponseFilterTestShouldReturnRequestUponNext() throws IOException, InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend responseToSend = new HttpResponseToSend(200, "Hello gorgeous");
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(responseToSend, new NextResponse()));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/asfa"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue(response.body().equals("Hello gorgeous"));
    }

    @Test
    public void nextResponseShouldNotBeReturnUponRequestForAdminPage() throws IOException, InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend responseToSend = new HttpResponseToSend(200, "Hello gorgeous");
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(responseToSend, new NextResponse()));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/admin"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue(response.body().trim().startsWith("<!DOCTYPE html"));
        Assert.assertTrue(response.body().trim().endsWith("</html>"));
    }

    @Test
    public void nextResponseShouldNotBeReturnUponRequestForApi() throws IOException, InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend responseToSend = new HttpResponseToSend(200, "Hello gorgeous");
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(responseToSend, new NextResponse()));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/api"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue(response.body().trim().startsWith("{"));
        Assert.assertTrue(response.body().trim().endsWith("}"));
    }

    @Test
    public void aNextFilterShouldOnlyReturnOnce() throws IOException, InterruptedException {
        RegisteredPreparedHttpResponses.getInstance().registeredResponses.clear();
        HttpResponseToSend responseToSend = new HttpResponseToSend(200, "Hello gorgeous");
        RegisteredPreparedHttpResponses.getInstance().add(new PreparedHttpResponse(responseToSend, new NextResponse()));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/asfa"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue(response.body().equals("Hello gorgeous"));

        HttpResponse<String> response2 = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assert.assertFalse(response2.statusCode() == 200);
        Assert.assertFalse(response2.body().equals("Hello gorgeous"));
    }


}

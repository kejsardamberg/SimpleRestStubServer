package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import org.junit.*;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.*;

public class HttpPluginTest extends ServerTestBase{


    @Test
    public void adminPageAccessible() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/admin"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue(response.body().trim().startsWith("<!DOCTYPE html"));
        Assert.assertTrue(response.body().trim().endsWith("</html>"));
    }

    @Test
    public void apiShouldRespondToGet() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/api"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(response.statusCode() == 200);
        Assert.assertTrue(response.body(), response.body().trim().startsWith("{"));
        Assert.assertTrue(response.body(), response.body().trim().endsWith("}"));
    }

    @Test
    public void getToUnregisteredPathShouldReturn404() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/ölfhewiutbngsda"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.assertTrue(response.statusCode() == 404);
    }

}
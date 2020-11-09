package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.NextResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import org.junit.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.Assert.*;

public class HttpPluginTest {

    static TestPlugin plugin;
    static HttpClient client;
    static int PORT = 8089;
    static Thread serverThread;

    @BeforeClass
    public static void setup() throws InterruptedException {
        plugin = new TestPlugin(PORT);
        serverThread = new Thread(plugin);
        serverThread.start();
        client = HttpClient.newHttpClient();
    }

    @AfterClass
    public static void teardown(){
    }

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

    @Test
    public void aNextFilterShouldOnlyReturnOnce() throws IOException, InterruptedException {
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

    @Test
    @Ignore
    public void postingNewNextRegisteredResponseShouldReturnIt() throws IOException, InterruptedException {
        //POST new RegisteredResponse
        String postBody = "{\"filters\":[{\"type\":\"next\"}], \"headers\":[], \"body\":\"Hello world\", \"bodyType\":\"direct\"}";
        System.out.println(postBody);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .header("Content-Type", "application/json")
                .uri(URI.create("http://127.0.0.1:" + PORT + "/api"))
                .build();
        HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Assert.assertTrue(response.statusCode() == 200);

        //GET to API to make sure it is registered
        HttpRequest getApiRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + PORT + "/api"))
                .build();
        HttpResponse<String> apiResponse = client.send(getApiRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(apiResponse.body());

        Assert.assertTrue(apiResponse.statusCode() == 200);
        Assert.assertTrue(apiResponse.body().contains("next"));

    }
}
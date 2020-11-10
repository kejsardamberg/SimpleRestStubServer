package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.http.HttpClient;

public class ServerTestBase {
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

    HttpResponse postAndGetResponse(String body) throws IOException {
        return postAndGetResponse(body, "/api");
    }

    HttpResponse getAndGetResponse(String endpoint){
        HttpURLConnection connection = null;
        HttpResponse response = new HttpResponse();
        try {
            //Create connection
            URL url = new URL("http://127.0.0.1:" + PORT + endpoint);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Origin", "http://127.0.0.1:" + PORT);
            connection.setRequestProperty("CustomHeaderName", "CustomHeaderValue");
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            StringBuilder content = null;

            try (var br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {

            String line;
            content = new StringBuilder();

            while ((line = br.readLine()) != null) {
                content.append(line);
                content.append(System.lineSeparator());
            }} catch (IOException e) {
                e.printStackTrace();
            }
            connection.getResponseCode();
            String responseBody = null;
            if(content != null){
                responseBody = content.toString();
            if(responseBody.length() > 0)
                responseBody = responseBody.substring(0, responseBody.length() - System.lineSeparator().length());
            }
            response.body = responseBody;
            response.responseCode = connection.getResponseCode();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }


    HttpResponse postAndGetResponse(String body, String endpoint) throws IOException {
        System.out.println(body);

        HttpResponse response = new HttpResponse();

        var myurl = new URL("http://127.0.0.1:" + PORT + endpoint);
        HttpURLConnection con = (HttpURLConnection) myurl.openConnection();

        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Java test client");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Language", "en-US");
        con.setRequestProperty("Origin", "http://127.0.0.1:" + PORT);

        try (var wr = new DataOutputStream(con.getOutputStream())) {

            wr.write(body.getBytes());

            StringBuilder content;

            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            response.responseCode = con.getResponseCode();
            System.out.println(content.toString());
            con.disconnect();
            String responseBody = content.toString();
            if(responseBody.length() > 0)
                responseBody = responseBody.substring(0, responseBody.length() - System.lineSeparator().length());
            response.body = responseBody;
            return response;
        } finally {
            con.disconnect();
        }
    }

    public static class TestPlugin extends HttpPlugin implements Runnable {


        public TestPlugin(Integer port) {
            super(port, false);
        }

        @Override
        public void run() {
            super.register();
        }
    }


}

package com.zingtongroup.servicevirtualizationserver.httptransportsupport.clientcomponents;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeader;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class HttpClient2 {

    private Map<String, String> washServerHeadersFromHeaders(Map<String, String> httpHeaders){
        Map<String, String> returnHeaders = new HashMap<>();
        for(String headerName : httpHeaders.keySet()){
            if(headerName.toLowerCase().trim().equals("connection"))continue;
            if(headerName.toLowerCase().trim().equals("accept-encoding"))continue;
            if(headerName.toLowerCase().trim().equals("referer"))continue;
            if(headerName.toLowerCase().trim().equals("host"))continue;
            returnHeaders.put(headerName, httpHeaders.get(headerName));
        }
        return returnHeaders;
    }

    public HttpResponseInternal sendRequest(com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpRequest httpRequest) throws IOException, InterruptedException {

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder()
                .uri(URI.create(httpRequest.getUrl()));

        if(httpRequest.getBody() == null){
            request.method(httpRequest.getVerb(), HttpRequest.BodyPublishers.noBody());
        } else {
            request.method(httpRequest.getVerb(), HttpRequest.BodyPublishers.ofString(httpRequest.getBody()));
        }
        for(String headerName : washServerHeadersFromHeaders(httpRequest.getHeaders()).keySet()){
            request.header(headerName, httpRequest.getHeaders().get(headerName));
        }

        HttpResponse<String> response = client.send(request.build(), HttpResponse.BodyHandlers.ofString());

        HttpResponseInternal returnResponse = new HttpResponseInternal();
        returnResponse.bodyContent = response.body();
        returnResponse.input = "HTTP/1.1 " + response.statusCode() + " OK";
        returnResponse.statusCode = response.statusCode();
        for(var head : response.headers().map().keySet()){
            returnResponse.headers.add(new HttpHeader(head, String.join(";", response.headers().map().get(head))));
        }
        System.out.println("Returned response from '" + httpRequest.getUrl() + "':\r\n" + returnResponse);
        return returnResponse;
    }
}


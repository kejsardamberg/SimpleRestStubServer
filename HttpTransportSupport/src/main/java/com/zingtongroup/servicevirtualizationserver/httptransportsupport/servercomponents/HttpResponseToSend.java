package com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpResponseToSend  {
    public int responseCode = 200;
    public String body;
    public String bodyFilePath;
    public HttpRequest httpRequest;
    public URI uri;
    public String bodyType;
    public com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeaders headers;

    HttpResponseToSend(){
        headers = new com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeaders();
    }

    public HttpResponseToSend(int responseCode, String bodyContent){
        this.responseCode = responseCode;
        this.body = bodyContent;
        headers = new com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeaders();
    }

    public void setUri(URI uri){
        this.uri = uri;
    }

    public void setHttpRequest(HttpRequest httpRequest){
        this.httpRequest = httpRequest;
    }

    public void setBodyFilePath(String path){
        bodyFilePath = path;
    }

    @JsonGetter("bodyFilePath")
    public String getBodyFilePath(){
        return bodyFilePath;
    }

    @JsonGetter("responseCode")
    public int statusCode() {
        return responseCode;
    }

    @JsonGetter("bodyType")
    public String getBodyType(){
        if(bodyType != null)return bodyType;
        if(body == null && bodyFilePath != null && bodyFilePath.length() > 0){
            bodyType = "file";
        } else {
            bodyType = "direct";
        }
        return bodyType;
    }

    @JsonGetter("httpRequest")
    public HttpRequest request() {
        return httpRequest;
    }

    @JsonIgnore
    public Optional<HttpResponse<String>> previousResponse() {
        return Optional.empty();
    }

    @JsonIgnore
    public HttpHeaders headers() {
        return null;
    }

    @JsonGetter("headers")
    public com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeaders getHeaders(){
        return headers;
    }

    @JsonGetter("body")
    public String body() {
        return body;
    }

    @JsonIgnore
    public Optional<SSLSession> sslSession() {
        return Optional.empty();
    }

    @JsonGetter("uri")
    public URI uri() {
        return uri;
    }

    @JsonIgnore
    public HttpClient.Version version() {
        return HttpClient.newHttpClient().version();
    }
}

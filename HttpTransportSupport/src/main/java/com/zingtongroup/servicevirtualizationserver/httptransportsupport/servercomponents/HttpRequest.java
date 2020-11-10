package com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zingtongroup.servicevirtualizationserver.interfaces.Inbound;
import com.zingtongroup.servicevirtualizationserver.interfaces.ServiceVirtualizationActivationFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest implements Inbound {
    private final Map<String, String> headers;
    private final String verb;
    private final String url;
    private final String body;
    private final String input;

    public HttpRequest(String input, String verb, String url, String body){
        this.headers = new HashMap<>();
        this.verb = verb;
        this.input = input;
        this.url = url;
        this.body = body;
    }

    public HttpRequest(String input, String verb, String url, String body, Map<String, String> headers){
        this.headers = headers;
        this.verb = verb;
        this.input = input;
        this.url = url;
        this.body = body;
    }

    @JsonIgnore
    public boolean isMatch(ServiceVirtualizationActivationFilter filter) {
        return false;
    }

    @JsonGetter("body")
    public String getBody(){
        return body;
    }

    @JsonGetter("verb")
    public String getVerb(){
        return verb;
    }

    @JsonGetter("url")
    public String getUrl(){
        return url;
    }

    @JsonGetter("headers")
    public Map<String, String> getHeaders(){
        return headers;
    }

    @JsonIgnore
    public String getOriginUrl(){
        for(String key : headers.keySet()){
            if(key.toLowerCase().equals("host") )
                return headers.get(key).trim();
        }
        for(String key : headers.keySet()){
            if(key.toLowerCase().equals("origin"))
                return headers.get(key).split("//")[1].split("/")[0].trim();
        }
        for(String key : headers.keySet()){
            if(key.toLowerCase().equals("referer")) return headers.get(key).split("//")[1].split("/")[0].trim();
        }
        return null;
    }

    @Override
    @JsonIgnore
    public String toString(){
        List<String> he = new ArrayList<>();
        for(String key : headers.keySet()){
            he.add(key + ": " + headers.get(key));
        }
        String bodyContent = "Empty body";
        if(body != null && body.length() > 0)
            bodyContent = body;

        return input + System.lineSeparator() + String.join(System.lineSeparator(), he) + System.lineSeparator() + bodyContent;
    }
}

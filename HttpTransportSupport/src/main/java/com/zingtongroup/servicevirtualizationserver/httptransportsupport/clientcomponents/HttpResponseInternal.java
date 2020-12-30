package com.zingtongroup.servicevirtualizationserver.httptransportsupport.clientcomponents;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeader;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeaders;

public class HttpResponseInternal {
    public String transport;
    public int statusCode;
    public String input;
    public String bodyContent;
    public HttpHeaders headers;
    public long responseTime;

    public HttpResponseInternal(){
        headers = new HttpHeaders();
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(input).append("\r\n");
        for(HttpHeader header : headers){
            sb.append(header.toString()).append("\r\n");
        }
        sb.append("\r\n");
        sb.append(bodyContent);
        return sb.toString();
    }
}

package com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.HeaderValueFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.HttpRequestFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.NextResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeader;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpRequest;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.interfaces.PreparedResponse;
import com.zingtongroup.servicevirtualizationserver.interfaces.ServiceVirtualizationActivationFilter;

import java.util.*;

public class PreparedHttpResponse implements PreparedResponse<HttpResponseToSend> {
    public List<HttpRequestFilter> filters;
    public HttpResponseToSend httpResponse;
    public UUID id;
    public int delay;

    PreparedHttpResponse(){
        id = UUID.randomUUID();
        this.filters = new ArrayList<>();
        delay = 0;
    }

    public PreparedHttpResponse(HttpResponseToSend httpResponse, HttpRequestFilter ... filters){
        this();
        this.httpResponse = httpResponse;
        if(filters == null) return;
        for(HttpRequestFilter filter : filters){
            this.filters.add(filter);
        }
    }

    @JsonIgnore
    public boolean isMatch(HttpRequest httpRequest){
        for(HttpRequestFilter filter : filters){
            filter.setHttpRequestForMatch(httpRequest);
            if(!filter.isMatch())
                return false;
        }
        return true;
    }

    public void setHttpRequestForResponse(HttpRequest httpRequest){
        httpResponse.setHttpRequest(httpRequest);
    }

    public void setNewResponse(HttpResponseToSend httpResponse){
        this.httpResponse = httpResponse;
    }

    @Override
    @JsonProperty("filters")
    public List<? extends ServiceVirtualizationActivationFilter> getFilters() {
        return filters;
    }

    @Override
    @JsonIgnore
    public HttpResponseToSend getResponse() {
        return httpResponse;
    }


}

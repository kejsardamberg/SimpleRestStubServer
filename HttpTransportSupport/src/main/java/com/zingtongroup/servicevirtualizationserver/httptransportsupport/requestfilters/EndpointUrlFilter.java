package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class EndpointUrlFilter extends HttpRequestFilter {

    EndpointUrlFilter() {} //For JSON

    public EndpointUrlFilter(String endpointUrl){
        this.field1 = endpointUrl;
    }

    @JsonIgnore
    public boolean isMatch() {
        return request != null && request.getUrl() != null && request.getUrl().equals(field1);
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "End-point path filter";
    }

    @Override
    @JsonIgnore
    public String toString(){
        return "End-point equals '" + field1 + "'";
    }
}

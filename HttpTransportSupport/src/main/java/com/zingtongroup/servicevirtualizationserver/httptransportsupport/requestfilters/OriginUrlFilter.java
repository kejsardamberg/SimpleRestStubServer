package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class OriginUrlFilter extends HttpRequestFilter {

    OriginUrlFilter(){} //For JSON

    public OriginUrlFilter(String originUrl){
        this.field1 = originUrl;
    }

    @JsonIgnore
    public boolean isMatch() {
        return request != null && request.getOriginUrl() != null && request.getOriginUrl().equals(field1);
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "Request origin filter";
    }

    @Override
    @JsonIgnore
    public String toString(){
        return "Request origin URL = '" + field1 + "'";
    }
}

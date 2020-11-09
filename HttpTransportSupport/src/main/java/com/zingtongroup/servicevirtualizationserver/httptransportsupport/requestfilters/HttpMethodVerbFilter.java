package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class HttpMethodVerbFilter extends HttpRequestFilter {

    HttpMethodVerbFilter(){} //For JSON

    public HttpMethodVerbFilter(String verb){
        field1 = verb;
    }

    @JsonIgnore
    @Override
    public boolean isMatch() {
        if(field1 == null)return false;
        return request != null &&
                request.getVerb() != null &&
                request
                        .getVerb()
                        .toUpperCase()
                        .trim()
                        .equals(field1
                                .toUpperCase()
                                .trim());
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "HTTP verb/method filter";
    }

    @Override
    @JsonIgnore
    public String toString(){
        return "Request method equals '" + field1.toUpperCase() + "'.";
    }
}

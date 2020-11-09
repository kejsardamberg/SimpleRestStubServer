package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class HeaderValueFilter extends HttpRequestFilter {

    HeaderValueFilter(){} //For JSON

    public HeaderValueFilter(String headerName, String headerValue){
        this.field1 = headerName;
        this.field2 = headerValue;
    }

    @JsonIgnore
    public boolean isMatch() {
        if(request == null || request.getHeaders().get(field1) == null) return false;
        return request.getHeaders().get(field1).equals(field2);
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "Header value filter";
    }

    @Override
    @JsonIgnore
    public String toString(){
        return "Request header value '" + field1 + "'='" + field2 + "'";
    }
}

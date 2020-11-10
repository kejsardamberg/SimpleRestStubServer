package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class HeaderExistFilter extends HttpRequestFilter {

    HeaderExistFilter(){} //For JSON

    public HeaderExistFilter(String headerName){
        this.field1 = headerName;
    }

    @JsonIgnore
    public boolean isMatch() {
        return request != null && request.getHeaders().get(field1.trim()) != null;
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "Header type filter";
    }

    @Override
    @JsonIgnore
    public String toString(){
        return "Request header '" + field1 + "' exist";
    }
}

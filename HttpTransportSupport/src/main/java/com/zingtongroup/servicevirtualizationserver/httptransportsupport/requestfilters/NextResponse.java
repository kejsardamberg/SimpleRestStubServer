package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class NextResponse extends HttpRequestFilter {

    public NextResponse(){} //For JSON

    @Override
    @JsonIgnore
    public boolean isMatch() {
        return true;
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "Next response to send";
    }

    @Override
    public String toString(){
        return "Send on next request";
    }
}

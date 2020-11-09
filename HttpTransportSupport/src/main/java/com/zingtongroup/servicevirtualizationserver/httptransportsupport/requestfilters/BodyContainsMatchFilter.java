package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class BodyContainsMatchFilter extends HttpRequestFilter {

    BodyContainsMatchFilter(){} //For JSON

    public BodyContainsMatchFilter(String containsStringPattern){
        this.field1 = containsStringPattern;
    }

    @Override
    @JsonIgnore
    public boolean isMatch() {
        return request != null && request.getBody() != null && request.getBody().contains(field1);
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "Request body contains string filter";
    }

    @Override
    @JsonIgnore
    public String toString(){
        return "Request body contains '" + field1 + "'";
    }
}

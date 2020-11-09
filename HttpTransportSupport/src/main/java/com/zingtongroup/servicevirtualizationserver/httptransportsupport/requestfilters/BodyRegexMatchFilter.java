package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class BodyRegexMatchFilter extends HttpRequestFilter {

    BodyRegexMatchFilter(){} //For JSON
    public BodyRegexMatchFilter(String matchPattern){
        this.field1 = matchPattern;
    }

    @Override
    @JsonIgnore
    public boolean isMatch() {
        return request != null && request.getBody() != null && request.getBody().matches(field1);
    }

    @Override
    @JsonIgnore
    public String friendlyName() {
        return "Request body regular expression filter";
    }

    @Override
    @JsonIgnore
    public String toString(){
        return "Request body matches regex pattern '" + field1 + "'";
    }
}

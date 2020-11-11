package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpRequest;
import com.zingtongroup.servicevirtualizationserver.interfaces.ServiceVirtualizationActivationFilter;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BodyRegexMatchFilter.class),
        @JsonSubTypes.Type(value = EndpointUrlFilter.class),
        @JsonSubTypes.Type(value = HeaderValueFilter.class),
        @JsonSubTypes.Type(value = HeaderExistFilter.class),
        @JsonSubTypes.Type(value = HttpMethodVerbFilter.class),
        @JsonSubTypes.Type(value = NextResponse.class),
        @JsonSubTypes.Type(value = OriginUrlFilter.class),
        @JsonSubTypes.Type(value = TimePeriodFilter.class),
        @JsonSubTypes.Type(value = BodyContainsMatchFilter.class)
})
@JsonIgnoreProperties(ignoreUnknown = true) // to ignore ALL unknown properties
public abstract class HttpRequestFilter implements ServiceVirtualizationActivationFilter {
    HttpRequest request;
    public String field1;
    public String field2;

    public HttpRequestFilter(){}

    @JsonProperty("type")
    public String getType(){
        return getClass().getSimpleName();
    }

    @JsonProperty("description")
    public String getDescription(){
        return toString();
    }

    public void setHttpRequestForMatch(HttpRequest httpRequestForMatch){
        request = httpRequestForMatch;
    }

}

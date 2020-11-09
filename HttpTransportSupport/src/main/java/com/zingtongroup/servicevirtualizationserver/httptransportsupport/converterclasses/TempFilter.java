package com.zingtongroup.servicevirtualizationserver.httptransportsupport.converterclasses;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.*;

public class TempFilter {
        public String type;
        public String field1;
        public String field2;
        public Object request;
        public String description;

        public TempFilter(){}

        public HttpRequestFilter toFilter() {
                switch (type) {
                        case "origin":
                                return new OriginUrlFilter(field1);
                        case "endpoint":
                                return new EndpointUrlFilter(field1);
                        case "next":
                                return new NextResponse();
                        case "verb":
                                return new HttpMethodVerbFilter(field1);
                        case "headername":
                                return new HeaderExistFilter(field1);
                        case "headervalue":
                                return new HeaderValueFilter(field1, field2);
                        case "bodycontains":
                                return new BodyContainsMatchFilter(field1);
                        case "bodyregex":
                                return new BodyRegexMatchFilter(field1);
                        default:
                                System.out.println("Exception: Cannot parse filter type '" + type + "'.");
                                return null;
                }

        }
}


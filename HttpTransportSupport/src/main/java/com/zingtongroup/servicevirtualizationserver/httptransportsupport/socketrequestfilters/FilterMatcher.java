package com.zingtongroup.servicevirtualizationserver.httptransportsupport.socketrequestfilters;

import java.util.ArrayList;

public class FilterMatcher extends ArrayList<RequestFilter> {

    public boolean isMatch(SocketRequest socketRequest){
        for(RequestFilter filter : this){
            if(!filter.test(socketRequest)) return false;
        }
        return true;
    }
}

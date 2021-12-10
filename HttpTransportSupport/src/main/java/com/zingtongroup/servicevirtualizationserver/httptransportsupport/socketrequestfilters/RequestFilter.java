package com.zingtongroup.servicevirtualizationserver.httptransportsupport.socketrequestfilters;

import java.util.function.Predicate;

public interface RequestFilter extends Predicate<SocketRequest> {

}

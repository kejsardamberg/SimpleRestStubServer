package com.zingtongroup.servicevirtualizationserver.interfaces;

public interface Inbound {

    boolean isMatch(ServiceVirtualizationActivationFilter filter);
}

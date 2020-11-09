package com.zingtongroup.servicevirtualizationserver.interfaces;

public interface NetworkTransportAdapter {

    void registerResponse(Object response, ServiceVirtualizationActivationFilter ... filters);
}

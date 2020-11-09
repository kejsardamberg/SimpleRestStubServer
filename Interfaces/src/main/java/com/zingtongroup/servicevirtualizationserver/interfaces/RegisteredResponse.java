package com.zingtongroup.servicevirtualizationserver.interfaces;

import java.util.Collection;

public interface RegisteredResponse {
    Object getResponseToReturn();
    Collection<ServiceVirtualizationActivationFilter> activationFilters();
}

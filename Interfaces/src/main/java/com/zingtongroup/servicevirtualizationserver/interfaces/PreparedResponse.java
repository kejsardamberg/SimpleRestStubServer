package com.zingtongroup.servicevirtualizationserver.interfaces;

import java.util.List;

public interface PreparedResponse<T> {
    List<? extends ServiceVirtualizationActivationFilter> getFilters();
    T getResponse();
}

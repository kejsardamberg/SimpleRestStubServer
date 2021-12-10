package com.zingtongroup.servicevirtualizationserver.httptransportsupport.socketrequestfilters;

public class ProtocolRequestFilter implements RequestFilter {

    private String matchingProtocol;

    public ProtocolRequestFilter(String matchingProtocol){
        this.matchingProtocol = matchingProtocol;
    }

    @Override
    public boolean test(SocketRequest socketRequest) {
        if(socketRequest == null || (socketRequest.protocol == null && matchingProtocol != null)) return false;
        if(socketRequest.protocol == null && matchingProtocol == null) return true;
        return socketRequest.protocol.equals(matchingProtocol);
    }
}

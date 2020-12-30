package com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents;

public class HttpHeader {
    public String name;
    public String value;

    HttpHeader(){}
    public HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString(){
        return name + ": " + value;
    }
}

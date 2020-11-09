package com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents;

import java.util.ArrayList;

public class HttpHeaders extends ArrayList<HttpHeader> {

    public String get(String name){
        for(HttpHeader header : this){
            if(header.name.equals(name))return header.value;
        }
        return null;
    }
}

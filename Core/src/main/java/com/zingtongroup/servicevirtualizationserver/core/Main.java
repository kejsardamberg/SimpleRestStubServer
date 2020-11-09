package com.zingtongroup.servicevirtualizationserver.core;


import com.zingtongroup.servicevirtualizationserver.httptransportsupport.HttpPlugin;

public class Main {

    public static void main(String[] args) {
        Integer port = null;
        for(String arg: args){
            try{
                Integer potentialNullValue = Integer.parseInt(arg);
                if(potentialNullValue != null && potentialNullValue > 0)
                    port = Integer.parseInt(arg);
            }catch (Exception e){
                //Ignored
            }
        }
        if(port == null){
            System.out.println("You may state server IP port as argument:\njava.exe -jar Stub-v1.0.0.jar [port]");
            port = 8089;
        }
        HttpPlugin plugin = new HttpPlugin(port);
        plugin.register();
    }

}

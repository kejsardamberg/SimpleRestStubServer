package com.zingtongroup.servicevirtualizationserver.core;


import com.zingtongroup.servicevirtualizationserver.httptransportsupport.HttpPlugin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        Integer port = null;

        //Use any give integer argument as TCP port
        for(String arg: args){
            try{
                int potentialNullValue = Integer.parseInt(arg);
                if(potentialNullValue > 0) port = Integer.parseInt(arg);
            }catch (NumberFormatException e){
                //Ignored
            }
        }

        //Set default value for port if none is read from arguments
        if(port == null){
            System.out.println("You may state server IP port as argument:" + System.lineSeparator()
                    + "java.exe -jar " + getExecutingFileName() + " [port]" + System.lineSeparator());
            port = 8089;
        }


        //Start HTTP server
        HttpPlugin plugin = new HttpPlugin(port);
        plugin.register();
    }

    public static String getExecutingFileName() throws URISyntaxException {
        try{
            String pathString = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String executingFilePath = URLDecoder.decode(pathString, StandardCharsets.UTF_8);
            return Path.of(executingFilePath).getFileName().toString();
        }catch (InvalidPathException e){ //Executing from IDE and not from jar file
            return "Main.class";
        }
    }

}

package com.zingtongroup.servicevirtualizationserver.httptransportsupport.socketrequestfilters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class SocketRequest {

    public String protocol;
    public final Map<String, String> headers;
    public String url;
    public String body;

    public SocketRequest(InputStream inputStream) throws IOException {
        headers = new HashMap<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream)); // we read characters from the client via input stream on the socket

        String input = in.readLine(); // get first line of the request from the client
        StringTokenizer parse = new StringTokenizer(input);// we parse the request with a string tokenizer
        protocol = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
        url = parse.nextToken().toLowerCase();// we get file requested
        if (url.endsWith("/")) {
            url += ".";
        }

        //code to read and print headers
        List<String> headerLines = new ArrayList<>();
        String headerLine;
        while((headerLine = in.readLine()).length() != 0){
            headerLines.add(headerLine);
        }

        //code to read the post payload data
        StringBuilder payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }

        body = payload.toString();
        for(String line : headerLines){ //Add headers
            int colonPosition = line.indexOf(":");
            if(colonPosition < 0) continue;
            String name = line.substring(0, colonPosition);
            String value = line.substring(colonPosition + 1);
            headers.put(name.trim(), value.trim());
        }

    }
}

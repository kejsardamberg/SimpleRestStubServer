package com.zingtongroup.servicevirtualizationserver.httptransportsupport.clientcomponents;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeader;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpRequest;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class HttpClient {
    int port;
    int timeoutMs = 30000;

    public HttpClient(int port){
        this.port = port;
    }

    public HttpResponseInternal send(HttpRequest request) throws IOException {
        String addressString = request.getUrl();
        String temp;
        long startTime = System.currentTimeMillis();

        //Read address from commandline
        InetAddress addr = InetAddress.getByName(addressString);

        //Open socket
        SocketAddress sockaddr = new InetSocketAddress(addr, port);
        Socket socket = new Socket();

        //Connection timeout
        socket.connect(sockaddr,timeoutMs);

        System.out.println( socket.getPort() +"\n");

        //Define input/output
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

        //Send GET request
        pw.print(request.getVerb() + " / HTTP/1.1\r\n");
        pw.print("Host: " + addressString + "\r\n\r\n");
        pw.flush(); // actually send data to server

        HttpResponseInternal response = new HttpResponseInternal();
        String path = null;
        String input = br.readLine(); // get first line of the request from the client
        response.input = input;
        StringTokenizer parse = new StringTokenizer(input);// we parse the request with a string tokenizer
        response.transport = parse.nextToken();
        String responseStatus = parse.nextToken(); // we get the HTTP method of the client
        response.statusCode = Integer.valueOf(responseStatus);

        //code to read and print headers
        List<String> headerLines = new ArrayList<>();
        String headerLine = null;
        while((headerLine = br.readLine()).length() != 0){
            headerLines.add(headerLine);
        }

        //code to read the post payload data
        StringBuilder payload = new StringBuilder();
        while(br.ready()){
            payload.append((char) br.read());
        }



/*

        while((temp=br.readLine())!=null){
            responseString.append(temp).append("\r\n");
        }
*/
        for(String line : headerLines){ //Add headers
            int colonPosition = line.indexOf(":");
            if(colonPosition < 0) continue;
            String name = line.substring(0, colonPosition);
            String value = line.substring(colonPosition + 1);
            response.headers.add(new HttpHeader(name.trim(), value.trim()));
        }

        response.responseTime = System.currentTimeMillis() - startTime;
        response.bodyContent = payload.toString();
        //Close Input/Output and Socket
        pw.close();
        br.close();
        socket.close();
        return response;
    }
}

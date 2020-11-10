package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.EndpointUrlFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeader;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpServer;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import com.zingtongroup.servicevirtualizationserver.interfaces.Plugin;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.util.Date;

public class HttpPlugin implements Plugin {

    public static int PORT = 8080;
    public HttpServer httpServer;
    private Thread serverThread;

    public HttpPlugin(Integer port){
        this(port, true);
    }

    public HttpPlugin(Integer port, boolean instatiateFromFile){
        if(port != null) PORT = port;
        if(instatiateFromFile)  RegisteredPreparedHttpResponses.getInstance().initiateFromFile();
        if(RegisteredPreparedHttpResponses.getInstance().registeredResponses.size() == 0){
            HttpResponseToSend  responseToSend = new HttpResponseToSend(200, "Hello world");
            responseToSend.headers.add(new HttpHeader("Content-Type", "text/plain"));
            PreparedHttpResponse response = new PreparedHttpResponse(responseToSend, new EndpointUrlFilter("/test"));
            RegisteredPreparedHttpResponses.getInstance().add(response, false);
        }
    }

    public void kill(){
        serverThread.interrupt();
    }

    @Override
    public void register() {
        try {
            ServerSocket serverConnect = new ServerSocket(PORT);
            System.out.println("Service Virtualization REST server");
            System.out.println("==================================");
            System.out.println("Admin interface at http://" + Inet4Address.getLocalHost().getHostAddress() + ":" + PORT + "/admin");
            System.out.println("API at http://" + Inet4Address.getLocalHost().getHostAddress() + ":" + PORT + "/api\n");
            System.out.println("Server started.\nListening for connections on port : " + PORT + " ...");

            // we listen until user halts server execution
            while (serverThread == null || serverThread.isAlive()) {
                httpServer = new HttpServer(serverConnect.accept());

                System.out.println("Connecton opened. (" + new Date() + ")");

                // create dedicated thread to manage the client connection
                serverThread = new Thread(httpServer);
                serverThread.start();
            }
            System.out.println("Shutting down server.");
        } catch (IOException e) {
            System.err.println("Server Connection error : " + e.getMessage());
        }
    }

}

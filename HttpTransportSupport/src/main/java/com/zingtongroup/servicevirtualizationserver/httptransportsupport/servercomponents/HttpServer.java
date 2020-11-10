package com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents;

import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.PreparedHttpResponse;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization.RegisteredPreparedHttpResponses;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.NextResponse;

import java.io.*;
import java.net.Socket;
import java.util.*;

// The tutorial can be found just here on the SSaurel's Blog :
// https://www.ssaurel.com/blog/create-a-simple-http-web-server-in-java
// Each Client Connection will be managed in a dedicated Thread
public class HttpServer implements Runnable{

    private Socket connect;// Client Connection via Socket Class
    static final boolean verbose = true; //Logging level
    BufferedReader in = null;
    PrintWriter out = null;
    BufferedOutputStream dataOut = null;
    boolean sent;
    HttpRequest request;

    public HttpServer(Socket c) {
        connect = c;
        sent = false;
    }

    @Override
    public void run() {
        // we manage our particular client connection
        sent = false;
        try {
            in = new BufferedReader(new InputStreamReader(connect.getInputStream())); // we read characters from the client via input stream on the socket
            out = new PrintWriter(connect.getOutputStream()); // we get character output stream to client (for headers)
            dataOut = new BufferedOutputStream(connect.getOutputStream()); // get binary output stream to client (for requested data)

            request = identifyRequest();
            System.out.println("Received request:" + System.lineSeparator() + request.toString());
            sendAdminPageIfApplicable();
            sendIpIfRequested();
            tendApiIfApplicable();
            sendAnyRegisteredNextResponse();
            sendMatchingRegisteredResponse();
            send404IfNothingSentSoFar();

        } catch (IOException ioe) {
            System.out.println("Server error : " + ioe);
        } finally {
            try {
                in.close();
                out.close();
                dataOut.close();
                connect.close(); // we close socket connection
            } catch (Exception e) {
                System.err.println("Error closing stream : " + e.getMessage());
            }

            if (verbose) {
                System.out.println("Connection closed.\n");
            }
        }
    }

    private void sendIpIfRequested() throws IOException {
        if(request.getUrl().toLowerCase().equals("/api/ip")){
            sent = true;
            send(request.getOriginUrl(), out, dataOut, null);
        }
    }

    private HttpRequest identifyRequest() throws IOException {
        String path = null;
        String input = in.readLine(); // get first line of the request from the client
        StringTokenizer parse = new StringTokenizer(input);// we parse the request with a string tokenizer
        String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
        path = parse.nextToken().toLowerCase();// we get file requested
        if (path.endsWith("/")) {
            path += ".";
        }

        //code to read and print headers
        List<String> headerLines = new ArrayList<>();
        String headerLine = null;
        while((headerLine = in.readLine()).length() != 0){
            headerLines.add(headerLine);
        }

        //code to read the post payload data
        StringBuilder payload = new StringBuilder();
        while(in.ready()){
            payload.append((char) in.read());
        }

        HttpRequest request = new HttpRequest(input, method, path, payload.toString());
        for(String line : headerLines){ //Add headers
            int colonPosition = line.indexOf(":");
            if(colonPosition < 0) continue;
            String name = line.substring(0, colonPosition);
            String value = line.substring(colonPosition + 1);
            request.getHeaders().put(name.trim(), value.trim());
        }
        return request;
    }

    private void sendAdminPageIfApplicable() throws IOException {
        if(request.getUrl().toLowerCase().equals("/admin")){
            sendAdminPage(out, dataOut);
            sent = true;
        }
    }

    private void send404IfNothingSentSoFar() {
        if(!sent) {
            send404(out);
            sent = true;
        }
    }

    private void sendMatchingRegisteredResponse() throws IOException {
        if(!sent){
            for(PreparedHttpResponse preparedHttpResponse : RegisteredPreparedHttpResponses.getInstance().registeredResponses){
                if(sent) break;
                if(preparedHttpResponse.isMatch(request)){
                    sent = true;
                    System.out.println("Sending prepared statement since match is found");
                    if((preparedHttpResponse.getResponse().body() == null ||
                            preparedHttpResponse.getResponse().body().length() == 0) &&
                            preparedHttpResponse.getResponse().getBodyFilePath() != null &&
                            preparedHttpResponse.getResponse().getBodyFilePath().length() > 0){
                        File file = new File(preparedHttpResponse.getResponse().getBodyFilePath());
                        StringBuilder sb = new StringBuilder();
                        try{
                            Scanner myReader = new Scanner(file);
                            while (myReader.hasNextLine()) {
                                sb.append(myReader.nextLine()).append(System.lineSeparator());
                            }
                            myReader.close();
                        }catch (Exception e){
                            sb.append(e);
                        }
                        preparedHttpResponse.setHttpRequestForResponse(request);
                    } else {
                        preparedHttpResponse.setHttpRequestForResponse(request);
                    }
                    sendResponse(out, dataOut, preparedHttpResponse);
                }
            }
        }
    }

    private void sendAnyRegisteredNextResponse() throws IOException {
        if(!sent){
            PreparedHttpResponse responseForRemoval = null;
            for(PreparedHttpResponse preparedHttpResponse : RegisteredPreparedHttpResponses.getInstance().registeredResponses){
                if(preparedHttpResponse.getFilters().stream().anyMatch(x -> x.getClass().isAssignableFrom(NextResponse.class))){
                    System.out.println("Sending prepared statement since match for next response is found");
                    preparedHttpResponse.setHttpRequestForResponse(request);
                    sendResponse(out, dataOut, preparedHttpResponse);
                    sent = true;
                    responseForRemoval = preparedHttpResponse;
                    break;
                }
            }
            if(responseForRemoval != null){
                RegisteredPreparedHttpResponses.getInstance().remove(responseForRemoval);
            }
        }
    }

    private void tendApiIfApplicable() throws IOException {
        if(request.getUrl().toLowerCase().equals("/api")){
            if(request.getVerb().equals("GET")){
                sendRegistered(out, dataOut);
                sent = true;
            } else if(request.getVerb().equals("POST")){
                System.out.println("Received new prepared HTTP response.");
                String body = request.getBody();
                String responseBody = RegisteredPreparedHttpResponses.getInstance().addFromJson(body);
                sendReceivedNotification(out, dataOut, responseBody);
                sent = true;
            } else if(request.getVerb().equals("DELETE") && request.getBody().matches("\\{\"id\":\".*\"\\}")){
                String id = request.getBody().substring(7, request.getBody().length() - 2);
                System.out.println("Deleting id: '" + id + "' from JSON '" + request.getBody() + "'");
                PreparedHttpResponse responseToDelete = null;
                for(PreparedHttpResponse preparedHttpResponse : RegisteredPreparedHttpResponses.getInstance().registeredResponses){
                    if(preparedHttpResponse.id.toString().equals(id)){
                        responseToDelete = preparedHttpResponse;
                        break;
                    }
                }
                RegisteredPreparedHttpResponses.getInstance().remove(responseToDelete);
                sent = true;
            }
        }
    }

    private void sendRegistered(PrintWriter out, OutputStream dataOut) throws IOException {
        String json = RegisteredPreparedHttpResponses.getInstance().toJson();
        out.println("HTTP/1.1 200 OK");
        out.println("Server: ServiceVirtualization");
        out.println("Date: " + new Date());
        out.println("Content-Type: application/json");
        out.println("Content-length: " + json.getBytes().length);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(json.getBytes(), 0, json.getBytes().length);
        dataOut.flush();

        System.out.println("Responded with registered prepared responses.");
    }

    private void send404(PrintWriter out) {
        out.println("HTTP/1.1 404 Not found");
        out.println("Server: ServiceVirtualization");
        out.println("Date: " + new Date());
        out.println("Content-length: " + 0);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer
    }

    private void sendReceivedNotification(PrintWriter out, OutputStream dataOut, String body) throws IOException {
        send(body, out, dataOut, null);
    }

    private void send(String body, PrintWriter out, OutputStream dataOut, Map<String, String> headers) throws IOException {
        if(body == null) body = "";
        byte[] bodyContent = body.getBytes();
        send(bodyContent, out, dataOut, headers);
    }

    private void send(byte[] bodyContent, PrintWriter out, OutputStream dataOut, Map<String, String> headers) throws IOException {
        if(headers == null) headers = new HashMap<>();
        out.println("HTTP/1.1 200 OK");
        out.println("Server: ServiceVirtualization");
        out.println("Date: " + new Date());
        for(String key : headers.keySet()){
            out.println(key + ": " + headers.get(key));
        }
        out.println("Content-length: " + bodyContent.length);
        out.println(); // blank line between headers and content, very important !
        out.flush(); // flush character output stream buffer

        dataOut.write(bodyContent, 0, bodyContent.length);
        dataOut.flush();
    }

    private void sendAdminPage(PrintWriter out, OutputStream dataOut) throws IOException {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");
        InputStream is = getClass().getClassLoader().getResourceAsStream("admin.html");;
        byte[] html = is.readAllBytes();
        send(html, out, dataOut, headers);
    }

    private void sendResponse(PrintWriter out, OutputStream dataOut, PreparedHttpResponse response) throws IOException {
        byte[] fileData = response.getResponse().body().getBytes();

        out.println("HTTP/1.1 " + response.getResponse().statusCode() + " OK");
        out.println("Server: ServiceVirtualization");
        out.println("Date: " + new Date());
        if(response != null && response.getResponse() != null && response.getResponse().headers() != null){
            for(String headerName : response.getResponse().headers().map().keySet()){
                out.println(headerName + ": " + response.getResponse().headers().map().get(headerName).toString());
            }
        }
        out.println("Content-length: " + fileData.length);
        out.println(); // blank line between headers and content, very important !
        /*
        if(response.getResponse().body() != null){
            //out.println(fileData);
            out.println(response.getResponse().body());
        }
         */
        out.flush(); // flush character output stream buffer

        dataOut.write(fileData, 0, fileData.length);
        dataOut.flush();

        System.out.println("Responded with prepared response.");
    }


}

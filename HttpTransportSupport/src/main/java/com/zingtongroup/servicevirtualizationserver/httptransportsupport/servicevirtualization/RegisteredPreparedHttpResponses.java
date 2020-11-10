package com.zingtongroup.servicevirtualizationserver.httptransportsupport.servicevirtualization;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.converterclasses.TempFilter;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters.*;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeader;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpHeaders;
import com.zingtongroup.servicevirtualizationserver.httptransportsupport.servercomponents.HttpResponseToSend;

import java.io.*;
import java.util.ArrayList;

public class RegisteredPreparedHttpResponses {

    public ArrayList<PreparedHttpResponse> registeredResponses;

    private RegisteredPreparedHttpResponses()
    {
        registeredResponses = new ArrayList<>();
    }

    public void initiateFromFile(){
        File settingsFile = new File("Stub_config.txt");
        if(!settingsFile.exists())return;
        ObjectMapper mapper = new ObjectMapper();
        //mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        try {
            PreparedHttpResponse[] temp = mapper.readValue(settingsFile, PreparedHttpResponse[].class);
            for(PreparedHttpResponse resp : temp){
                this.add(resp, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(PreparedHttpResponse preparedHttpResponse){
        add(preparedHttpResponse, true);
    }

    public void add(PreparedHttpResponse preparedHttpResponse, boolean saveToFile){
        registeredResponses.add(preparedHttpResponse);
        if(saveToFile) saveToFile();
    }

    public void remove(Object preparedHttpResponse){
        registeredResponses.remove(preparedHttpResponse);
        saveToFile();
    }

    public void saveToFile(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        File settingsFile = new File("Stub_config.txt");
        try {
            mapper.writeValue(settingsFile, registeredResponses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Inner class to provide instance of class
    private static class BillPughSingleton
    {
        private static final RegisteredPreparedHttpResponses INSTANCE = new RegisteredPreparedHttpResponses();
    }

    @JsonIgnore
    public String toJson(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String addFromJson(String json) {
        System.out.println("Parsing json to PreparedHttpResponse: " + json);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        try {
            PreparedHttpResponse preparedHttpResponse = mapper.readValue(json, PreparedHttpResponse.class);
            this.add(preparedHttpResponse);
            return mapper.writeValueAsString(preparedHttpResponse);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
            this.add(new PreparedHttpResponse(new HttpResponseToSend(200, json), new NextResponse()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static RegisteredPreparedHttpResponses getInstance()
    {
        return BillPughSingleton.INSTANCE;
    }
}


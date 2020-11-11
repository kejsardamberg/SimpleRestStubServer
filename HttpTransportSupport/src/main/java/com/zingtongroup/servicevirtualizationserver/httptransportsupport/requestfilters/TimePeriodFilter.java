package com.zingtongroup.servicevirtualizationserver.httptransportsupport.requestfilters;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonTypeInfo( use= JsonTypeInfo.Id.MINIMAL_CLASS)
public class TimePeriodFilter extends HttpRequestFilter {

    TimePeriodFilter(){} //For JSON

    public TimePeriodFilter(String startTime, String stopTime){
        this.field1 = startTime;
        this.field2 = stopTime;
    }

    @Override
    @JsonIgnore
    public boolean isMatch() {
        Date startTime = null;
        Date stopTime = null;
        try{
            startTime = new SimpleDateFormat("HH:mm:SS").parse(field1);
            stopTime = new SimpleDateFormat("HH:mm:SS").parse(field2);
        } catch (ParseException e) {
            System.out.println("ERROR: Exception - Date parse exception. Expected time format: 'HH:mm:SS'");
            return false;
        }
        if(startTime == null || stopTime == null) return false;
        Date now = new Date();
        return startTime.before(now) && now.before(stopTime);
    }

    @JsonIgnore
    @Override
    public String friendlyName(){
        return "Time period match filter";
    }

    @Override
    @JsonIgnore
    public String toString() {
        return "Time is between '" + field1 + "' and '" + field2 + "'";
    }
}

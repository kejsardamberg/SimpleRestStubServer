package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class SettingsManager extends Properties {

    public SettingsManager(){

    }

    public void saveToFile(String fileName) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
        FileWriter writer = new FileWriter(fileName);
        store(writer, "Saving changes " + sdf.format(new Date()));
    }
}

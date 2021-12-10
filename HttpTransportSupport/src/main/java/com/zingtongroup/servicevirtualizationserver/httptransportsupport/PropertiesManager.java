package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * General class for managing program runtime properties in key-value-pairs
 */
public class PropertiesManager {

    private static PropertiesManager singletonInstance = null;
    private static Map<String, String> defaultValues = new HashMap<>();
    private final Properties properties;

    private PropertiesManager() throws IOException {
        properties = new Properties(null);
    }

    public PropertiesManager(String filePath) throws IOException {
        properties = new Properties(filePath);
    }

    /**
     * Used before initiating PropertiesManager to register default values. Default values can be altered or temporarily overridden.
     * @param keyValuePairs
     */
    public static void setDefaultsBeforeInitiation(Map<String, String> keyValuePairs){
        defaultValues = keyValuePairs;
    }

    private static void loadRegisteredDefaultKeyValuePairs(){
        for(String key : defaultValues.keySet()){
            singletonInstance.properties.add(key, defaultValues.get(key));
        }
    }

    /**
     * For use with Main method String array arguments.
     * If any argument correspond with an existing valid properties file the argument is used as a properties file.
     * If any argument is a key-value-pair it is used as a temporary override for property value.
     * @param args Main method arguments (String array)
     * @return Returns the singleton instance of Properties
     * @throws IOException
     */
    public static Properties initiateWithMainMethodArguments(String[] args) throws IOException {
        for(String arg : args){
            if(Files.exists(Path.of(arg.trim())) && isValidPropertiesFile(arg.trim())){
                singletonInstance = new PropertiesManager(arg);
            }
        }

        if (singletonInstance == null)
        {
            singletonInstance = new PropertiesManager();
        }

        loadRegisteredDefaultKeyValuePairs();

        for(String arg : args){
            if(arg.length() - arg.replace("=", "").length() != 1) continue;
            if(arg.trim().contains(" ")) continue;
            String key = arg.trim().split("=")[0];
            String value = arg.trim().substring(key.length() + 1);
            TempKeyValuePair tempKeyValuePair = new TempKeyValuePair();
            tempKeyValuePair.key = key;
            tempKeyValuePair.value = value;
            singletonInstance.properties.add(tempKeyValuePair);
        }
        return singletonInstance.properties;
    }

    /**
     * Tries to read properties from file. Any runtime changes to properties are saved to the file.
     * @param propertiesFilePath File path for properties file
     * @return Returns a singleton instance of Properties
     * @throws IOException
     */
    public static Properties initiateWithFile(String propertiesFilePath) throws IOException {
        if (singletonInstance == null)
        {
            singletonInstance = new PropertiesManager(propertiesFilePath);
            loadRegisteredDefaultKeyValuePairs();
        }
        return singletonInstance.properties;
    }

    /**
     * Checcks if a file is a valid properties file
     * @param filePath
     * @return
     * @throws IOException
     */
    public static boolean isValidPropertiesFile(String filePath) throws IOException {
        if(Files.notExists(Path.of(filePath))) return false;
        String fileContent = Files.readString(Path.of(filePath));
        String[] fileRows = fileContent.split(System.lineSeparator());
        int numberOfRowsWithKeyValuePairs = 0;
        for(String fileRow : fileRows){
            if(fileRow == null || fileRow.length() < 1 || fileRow.trim().startsWith("#") || !fileRow.contains("=")) continue;
            if(fileRow.substring(1).length() - fileRow.substring(1).replace("=", "").length() > 1) return false;
            if(fileRow.trim().contains(" ")) return false;
        }
        return true;
    }

    /**
     * Singleton instance of current Properties.
     * @return Singleton instance of current Properties.
     * @throws IOException
     */
    public static Properties getPropertiesInstance() throws IOException {
        if (singletonInstance == null)
        {
            singletonInstance = new PropertiesManager();
            loadRegisteredDefaultKeyValuePairs();
        }
        return singletonInstance.properties;
    }

    /***
     * This class handles persisted Properties for runtime settings.
     */
    public class Properties extends ArrayList<KeyValuePair> {

        public String fileName;

        public Properties(String fileName) throws IOException {
            this.fileName = fileName;
            getUsableFileName();
            loadDefaults();
            if(Files.notExists(Path.of(this.fileName))){
                Files.createFile(Path.of(this.fileName));
                saveChanges();
            }
            String fileContent = Files.readString(Path.of(this.fileName));
            String[] fileRows = fileContent.split(System.lineSeparator());
            for(String fileRow : fileRows){
                if(fileRow == null || fileRow.length() < 1 || fileRow.trim().startsWith("#") || !fileRow.contains("=")) continue;
                String key = fileRow.split("=")[0].trim();
                String value = fileRow.substring(fileRow.indexOf("=") + 1).trim();
                this.add(key, value);
            }
        }

        private void loadDefaults(){
            this.add("apiEndpointRootPath", "/api");
            this.add("adminGuiEndpoint", "/admin");
        }

        public String get(String key){

            for (KeyValuePair keyValuePair : this){ //Return value for any matching temporarily added keys first
                if(!keyValuePair.getClass().isAssignableFrom(TempKeyValuePair.class)) continue;
                if(keyValuePair.key.equals(key)) return keyValuePair.value;
            }

            for (KeyValuePair keyValuePair : this){ //Return value for any matching keys
                if(keyValuePair.key.equals(key)) return keyValuePair.value;
            }

            return null; // No match returns null
        }

        public boolean usePropertyForThisSessionOnly(String key, String value){
            for(KeyValuePair keyValuePair1 : this){
                if(!keyValuePair1.getClass().isAssignableFrom(TempKeyValuePair.class))continue;
                if(keyValuePair1.key.equals(key)){
                    keyValuePair1.value = value;
                    saveChanges();
                    return true;
                }
            }
            TempKeyValuePair keyValuePair = new TempKeyValuePair();
            keyValuePair.key = key;
            keyValuePair.value = value;
            return this.add(keyValuePair);
        }

        public boolean add(String key, String value){
            for(KeyValuePair keyValuePair1 : this){
                if(keyValuePair1.key.equals(key)){
                    keyValuePair1.value = value;
                    saveChanges();
                    return true;
                }
            }
            KeyValuePair keyValuePair = new KeyValuePair();
            keyValuePair.key = key;
            keyValuePair.value = value;
            boolean returnValue = this.add(keyValuePair);
            saveChanges();
            return returnValue;
        }

        private void saveChanges() {
            StringBuilder sb = new StringBuilder();
            for(KeyValuePair keyValuePair : this){
                if(keyValuePair.getClass().isAssignableFrom(TempKeyValuePair.class)) continue;
                sb.append(keyValuePair.key).append("=").append(keyValuePair.value).append(System.lineSeparator());
            }
            File file = new File(fileName);
            FileWriter fileWriter;
            try{
                fileWriter = new FileWriter(file, false); // false to overwrite.
                fileWriter.write(sb.toString());
                fileWriter.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }


        private void getUsableFileName() {

            if(fileName != null) return;

            Path fileNameWithoutPath = Path.of(getExecutingFilePath()).getFileName();

            String fileNameWithoutExtension;
            if (fileNameWithoutPath.toString().indexOf(".") > 0) {
                fileNameWithoutExtension = fileNameWithoutPath.toString().substring(0, fileNameWithoutPath.toString().lastIndexOf("."));
            } else {
                fileNameWithoutExtension = fileNameWithoutPath.toString();
            }

            if(fileNameWithoutExtension == null) fileNameWithoutExtension = "StubServer";
            fileName = fileNameWithoutExtension + ".properties";

        }

        private String getExecutingFilePath() {
            try{
                String pathString = Properties.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                String returnValue = URLDecoder.decode(pathString, StandardCharsets.UTF_8);
                Path.of(returnValue); //Test
                return  returnValue;
            }catch (InvalidPathException e){ //Executed from IDE, not from jar file
                return "StubServer.jar";
            }
        }

        public List<String> getKeysList() {
            List<String> keys = new ArrayList<>();
            for(KeyValuePair keyValuePair : this){
                keys.add(keyValuePair.key);
            }
            return keys;
        }

    }

    public static class TempKeyValuePair extends KeyValuePair{}

    public static class KeyValuePair{
        public String key;
        public String value;
    }

}


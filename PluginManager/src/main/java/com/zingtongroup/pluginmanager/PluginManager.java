package com.zingtongroup.pluginmanager;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginManager<T>  {

    private ArrayList<Class<T>> pluginClasses;
    private ArrayList<T> pluginInstances;
    private Class<T> type;

    public PluginManager(Class<T> clazz){
        pluginClasses = new ArrayList<>();
        pluginInstances = new ArrayList<>();
        this.type = clazz;
    }

    public void loadPlugin(Class<T> classToLoad) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        getPluginClasses().add(classToLoad);
        getPluginInstances().add(classToLoad.getConstructor(null).newInstance());
    }

    public Collection<Class<T>> getPluginClasses(){
        return pluginClasses;
    }

    public Collection<T> getPluginInstances(){
        return pluginInstances;
    }

    public Class<T> getType(){
        return type;
    }

    private static <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch(ClassCastException e) {
            return null;
        }
    }

    private boolean jarFileContainsClassFromT(File jar) throws IOException, ClassNotFoundException {
        JarFile jarFile = null;
        try{
            jarFile = new JarFile(jar);
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + jar.toPath() +"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);
        boolean returnValue = false;

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            // -6 because of .class
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            Class c = cl.loadClass(className);
            if(!c.isAssignableFrom(type))continue;
            returnValue = true;
        }
        return returnValue;
    }

    private void loadAllPluginJarsInDirectory(String dirName) throws IOException, ClassNotFoundException {
        File dir = new File(dirName);
        File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));
        for(File jarFile : files){
            if(jarFileContainsClassFromT(jarFile)){
                System.out.println("Found plugin in file '" + jarFile.getName());
            }
        }
    }

    public void initiateFromCurrentFolder() throws IOException, ClassNotFoundException {
        loadAllPluginJarsInDirectory(System.getProperty("user.dir"));

        Instrumentation inst = InstrumentHook.getInstrumentation();
        for (Class<?> clazz: inst.getAllLoadedClasses()) {
            if(!clazz.isAssignableFrom(type))continue;
            getPluginClasses().add((Class<T>) clazz);
            try {
                Object o = clazz.getConstructor(null).newInstance(null);
                System.out.println("Class identified and instaciated: " + o.getClass().getName());
                getPluginInstances().add(convertInstanceOfObject(o, type));
            }catch (Exception e){
                System.out.println("Exception caught ="+e.getMessage());
            }
        }


    }

}

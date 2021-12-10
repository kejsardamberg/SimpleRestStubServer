package com.zingtongroup.pluginmanager;

import org.junit.Test;

import java.io.IOException;

public class PluginManagerTests {

    @Test
    public void loadPlugin() throws IOException, ClassNotFoundException {
        PluginManager<TestPlugin> pluginManager = new PluginManager(TestPlugin.class);
        pluginManager.initiateFromCurrentFolder();
        for(TestPlugin tp : pluginManager.getPluginInstances()){
            tp.executeMethod();
        }
    }
}

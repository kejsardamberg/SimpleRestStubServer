package com.zingtongroup.servicevirtualizationserver.httptransportsupport;

import java.net.http.HttpClient;

public class TestPlugin extends HttpPlugin implements Runnable {

    public TestPlugin(Integer port) {
        super(port);
    }

    @Override
    public void run() {
        super.register();
    }
}

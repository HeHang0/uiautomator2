package com.oohoo.uiautomator2;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

@RunWith(AndroidJUnit4.class)
public class DumpHierarchyServerTest {
    final String TAG = "UiA2[DumpHierarchy]";
    private UiDevice mDevice;
    private int httpPort = 38179;

    @Test
    public void dumpTreeServer() throws Exception {
        // adb shell am instrument --user 0 -w -r -e port  -e debug false -e class com.oohoo.uiautomator2.DumpHierarchyServerTest#dumpTreeServer com.oohoo.uiautomator2/androidx.test.runner.AndroidJUnitRunner
        Log.i(TAG, "DumpHierarchyServerTest Start");
        if(!this.initDevice()){
            return;
        }
        this.startServer();
        while (true) {
            Thread.sleep(10 * 1000);
        }
    }

    private boolean initDevice() {
        try {
            mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            return true;
        }catch (Exception e) {
            Log.e(TAG, "Init UiDevice Error: " + e.getMessage());
            return false;
        }
    }

    private void startServer() throws IOException {
        Bundle extras = InstrumentationRegistry.getArguments();
        if (extras.containsKey("port")) {
            try {
                httpPort = Integer.parseInt(extras.getString("port"));
            }catch (Exception e) {
                Log.e(TAG, "Parse Port Error, Use Default Port: " + httpPort);
            }
        }
        Log.i(TAG, "Server start at port: " + httpPort);
        HttpService httpService = new HttpService(httpPort, mDevice);
        httpService.start();
    }
}

class HttpService extends NanoHTTPD {
    final String TAG = "UiA2[HttpService]";
    private final UiDevice mDevice;
    //???????????? ????????????
    public HttpService(int port, UiDevice dvc){
        super(port);
        this.mDevice = dvc;
    }

    private String lastXml = "";
    private long lastXMLSecond = 0;

    //??????Serve??????????????????????????????????????????
    @Override
    public Response serve(IHTTPSession session) {
        //????????????uri
        String uri = session.getUri();
        //???????????????????????????uri??????
        String res = "";
        if(uri.equals("/tree")){
            try {
                if(System.currentTimeMillis() - lastXMLSecond > 1000){
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    mDevice.dumpWindowHierarchy(output);
                    lastXml = output.toString();
                    lastXMLSecond = System.currentTimeMillis();
                }
                res = lastXml;
            } catch (IOException e) {
                Log.e(TAG, "dumpWindowHierarchy: " + e.getMessage());
            }
        }else if(uri.equals("/hello")) {
            res = "Server Alive";
        }
        return NanoHTTPD.newFixedLengthResponse(res);
    }
}
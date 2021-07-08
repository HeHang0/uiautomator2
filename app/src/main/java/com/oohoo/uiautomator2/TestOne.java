package com.oohoo.uiautomator2;

import android.util.Log;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

@RunWith(AndroidJUnit4.class)
public class TestOne {
    final String TAG = "UiA2[TestOne]";
    private UiDevice mDevice;
    @Test
    public void demo() throws Exception {
        // adb shell am instrument --user 0 -w -r -e debug false -e class com.oohoo.uiautomator2.TestOne#demo com.oohoo.uiautomator2.test/androidx.test.runner.AndroidJUnitRunner
        this.initDevice();
        this.startServer();
        while (true) {
            Thread.sleep(10000);
            Log.i(TAG, "Server Alive");
        }
    }

    private boolean initDevice() {
        try {
            mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    private void startServer() throws Exception {
        HttpService httpService = new HttpService(8182, mDevice);
        httpService.start();
    }
}

class HttpService extends NanoHTTPD {
    final String TAG = "UiA2[HttpService]";
    private final UiDevice mDevice;
    //构造函数 赋值父类
    public HttpService(int port, UiDevice dvc){
        super(port);
        this.mDevice = dvc;
    }

    //重写Serve方法，每次请求时会调用该方法
    @Override
    public Response serve(IHTTPSession session) {
        //获取请求uri
//        String uri = session.getUri();
        //这里默认把接收到的uri返回
        String res = "";
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            mDevice.dumpWindowHierarchy(output);
            res = output.toString();
        } catch (IOException e) {
            res = e.getMessage();
            Log.i(TAG, "dumpWindowHierarchy: " + res);
        }
        return NanoHTTPD.newFixedLengthResponse(res);
    }
}
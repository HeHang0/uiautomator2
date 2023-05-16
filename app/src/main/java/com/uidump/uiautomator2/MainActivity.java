package com.uidump.uiautomator2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.test.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    final String TAG = "UiA2[MainActivity]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button getTreeBtn = findViewById(R.id.getTree);
        getTreeBtn.setOnClickListener(this::runMyUiautomator);
    }

    public void runMyUiautomator(View v){
        Log.i(TAG, "runMyUiautomator: ");
        try {

            UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        ByteArrayOutputStream output = new ByteArrayOutputStream();
//        mDevice.dumpWindowHierarchy(output);
            mDevice.pressHome();
            mDevice.pressHome();
            UiObject x=mDevice.findObject(new UiSelector().text("联系人"));
            x.click();
        }catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
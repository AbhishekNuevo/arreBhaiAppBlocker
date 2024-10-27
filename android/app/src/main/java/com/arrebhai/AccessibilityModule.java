package com.arrebhai;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;

public class AccessibilityModule extends ReactContextBaseJavaModule {
    public AccessibilityModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return "AccessibilityModule";
    }

    @ReactMethod
    public void startAccessibilityService(String log) {
        Toast.makeText(this.getReactApplicationContext(),"This is from Native Android",Toast.LENGTH_LONG).show();
        // Code to start and manage accessibility service
    }
}

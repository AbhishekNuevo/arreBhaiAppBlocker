package com.arrebhai;

import android.content.Context;
import android.content.SharedPreferences;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

public class SharedPreferencesModule extends ReactContextBaseJavaModule {
    private static final String PREFS_NAME = "ReactNativePreferences";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesModule(ReactApplicationContext reactContext) {
        super(reactContext);
        sharedPreferences = reactContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public String getName() {
        return "SharedPreferencesModule";
    }

    @ReactMethod
    public void setValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @ReactMethod
    public void getValue(String key, Promise promise) {
        String value = sharedPreferences.getString(key, null);
        promise.resolve(value);
    }
}

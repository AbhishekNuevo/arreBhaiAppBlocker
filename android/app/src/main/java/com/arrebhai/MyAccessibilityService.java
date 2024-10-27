package com.arrebhai;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyAccessibilityService extends AccessibilityService {
    String tag = "ArreBhai";
    private void sendEventToReactNative(String eventName, String eventData) {
        ReactApplication reactApplication = (ReactApplication) getApplication();
        ReactContext reactContext = reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext();
        if (reactContext != null && reactContext.hasActiveCatalystInstance()) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, eventData);
        } else {
            Log.d(tag, "ReactContext is not initialized. Event not emitted.");
        }
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Monitor application package changes and detect browser URLs
//        Log.d(tag, "App in foreground:1 " + event);
        String packageName = event.getPackageName() != null ? event.getPackageName().toString() : "";
        SharedPreferences sharedPreferences = getSharedPreferences("ReactNativePreferences", Context.MODE_PRIVATE);
        String timeUnlock = sharedPreferences.getString("unlockTime", "0");
        String blockedApps = sharedPreferences.getString("blockedApps", null);
        boolean isBloackedApp =  getBloackedApp(blockedApps,packageName,"");

        boolean isBlock = true;
        long currentTime = System.currentTimeMillis();
        if(isValidLong(timeUnlock) && Long.parseLong(timeUnlock) > 0){

            isBlock =  (currentTime - Long.parseLong(timeUnlock)) > 0;
        }

        Log.d(tag, "packageName->>" + packageName + "  " + timeUnlock + " isValid " + isValidLong(timeUnlock) + " greater " + (Long.parseLong(timeUnlock) > 0) + " currentTime " + currentTime + " isBlock " + isBlock + " " + (currentTime - Long.parseLong(timeUnlock)));
//        sendEventToReactNative("onAccessibilityEvent", packageName);
        if(!isBlock) return;

        if(isBloackedApp && isBlock){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // Required flag for starting an activity from a service
            startActivity(intent);
            sendEventToReactNative("onAccessibilityEvent", packageName);
            return;
        }

        if (isBrowserApp(packageName)) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            Log.d(tag, "nodeInfo " + nodeInfo);

            // String packageName = event.getPackageName().toString();
            SupportedBrowserConfig browserConfig = null;
            for (SupportedBrowserConfig supportedConfig: getSupportedBrowsers()) {
                if (supportedConfig.packageName.equals(packageName)) {
                    browserConfig = supportedConfig;
                }
            }
            //this is not supported browser, so exit
            if (browserConfig == null) {
                Log.d(tag, "this is not supported browser, " + packageName);
                return;
            }
            AccessibilityNodeInfo parentNodeInfo = event.getSource();
            if (parentNodeInfo == null) {
                return;
            }


            String capturedUrl = captureUrl(parentNodeInfo, browserConfig);
            parentNodeInfo.recycle();

            //we can't find a url. Browser either was updated or opened page without url text field
            if (capturedUrl == null) {
                Log.d(tag, "URL Not found ->>>>> " + packageName +" "+ parentNodeInfo );
                return;
            }

            long eventTime = event.getEventTime();
            String detectionId = packageName + ", and url " + capturedUrl;

            Log.d(tag, "URL->>>>> " + eventTime + " "+ detectionId + " " + timeUnlock);


            if(getBloackedApp(blockedApps,"",capturedUrl) && isBlock){
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  // Required flag for starting an activity from a service
                startActivity(intent);
                sendEventToReactNative("onAccessibilityEvent", packageName);
            }



        }

    }

    @Override
    public void onServiceConnected() {
        // pass the typeof events you want your service to listen to
        // other will not be handledby this service
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_CLICKED |
                AccessibilityEvent.TYPE_VIEW_FOCUSED | AccessibilityEvent.TYPES_ALL_MASK;

        // In case you want your service to work only with a particular application
        //or when that application is in foreground, you should specify those applications package
        //names here, otherwise the service would listen events from all the applications
        // info.packageNames = AccessibilityServiceInfo.Pa

        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_ALL_MASK ;
        // the notification timeout is the time interval after which the service would
        // listen from the system. Anything happening between that interval won't be
        // captured by the service
        info.notificationTimeout = 100;

        // finally set the serviceInfo
        this.setServiceInfo(info);

    }



    @Override
    protected boolean onKeyEvent(KeyEvent event) {

        int action = event.getAction();
        int keyCode = event.getKeyCode();
        // the service listens for both pressing and releasing the key
        // so the below code executes twice, i.e. you would encounter two Toasts
        // in order to avoid this, we wrap the code inside an if statement
        // which executes only when the key is released
        if (action == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                Log.d(tag, "KeyUp");
                Toast.makeText(this, "KeyUp", Toast.LENGTH_SHORT).show();
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                Log.d(tag, "KeyDown");
                Toast.makeText(this, "KeyDown", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onKeyEvent(event);
    }


    @Override
    public void onInterrupt() {
        // Handle service interruption
        Log.d(tag, "App in foreground: ");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(tag, "onUnbind ");
        return super.onUnbind(intent);

    }

    // Detect whether the current app is a browser
    private boolean isBrowserApp(String packageName) {
        // You can add other browsers' package names here
        return packageName.contains("com.android.chrome") || packageName.contains("com.brave.browser");
    }

    // Extract the URL from the browser window's AccessibilityNodeInfo
    private String getUrlFromBrowser(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) return null;

        if (nodeInfo.getClassName().toString().contains("EditText")) {
            CharSequence content = nodeInfo.getText();
            if (content != null) {
                return content.toString();
            }
        }

        // Recursively check child nodes
        for (int i = 0; i < nodeInfo.getChildCount(); i++) {
            AccessibilityNodeInfo childNode = nodeInfo.getChild(i);
            String result = getUrlFromBrowser(childNode);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private String captureUrl(AccessibilityNodeInfo info, SupportedBrowserConfig config) {
        List<AccessibilityNodeInfo> nodes = info.findAccessibilityNodeInfosByViewId(config.addressBarId);
        if (nodes == null || nodes.size() <= 0) {
            return null;
        }

        AccessibilityNodeInfo addressBarNodeInfo = nodes.get(0);
        String url = null;
        if (addressBarNodeInfo.getText() != null) {
            url = addressBarNodeInfo.getText().toString();
        }
        addressBarNodeInfo.recycle();
        return url;
    }

    @NonNull
    private static String[] packageNames() {
        List<String> packageNames = new ArrayList<>();
        for (SupportedBrowserConfig config: getSupportedBrowsers()) {
            packageNames.add(config.packageName);
        }
        return packageNames.toArray(new String[0]);
    }

    private static class SupportedBrowserConfig {
        public String packageName, addressBarId;
        public SupportedBrowserConfig(String packageName, String addressBarId) {
            this.packageName = packageName;
            this.addressBarId = addressBarId;
        }
    }

    /** @return a list of supported browser configs
     * This list could be instead obtained from remote server to support future browser updates without updating an app */
    @NonNull
    private static List<SupportedBrowserConfig> getSupportedBrowsers() {
        List<SupportedBrowserConfig> browsers = new ArrayList<>();
        browsers.add( new SupportedBrowserConfig("com.android.chrome", "com.android.chrome:id/url_bar"));
        browsers.add( new SupportedBrowserConfig("org.mozilla.firefox", "org.mozilla.firefox:id/url_bar_title"));
        browsers.add( new SupportedBrowserConfig("com.brave.browser", "com.brave.browser:id/url_bar"));

        return browsers;
    }

    private boolean isValidLong(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean getBloackedApp(String json,String packageName, String url) {
        if (json != null && !json.isEmpty()) {
            // Use Gson to parse JSON back to List<Long> or List<Integer>
            Gson gson = new Gson();
            Type type = new TypeToken<List<BlockedApp>>() {}.getType();
            List<BlockedApp> blockedAppList = gson.fromJson(json, type);

            // Iterate over the list and log each bundleId (package name)
            for (BlockedApp app : blockedAppList) {
                Log.d(tag, "Package name (bundleId): " + app.getBundleId()+ "pack "+ packageName + " url " + url);
                if(!packageName.isEmpty() && !app.getBundleId().isEmpty() && packageName.contains(app.getBundleId())){
                    return true;
                }else if(packageName.isEmpty() && !url.isEmpty() && !app.getUrl().isEmpty() && url.contains(app.getUrl())){
                    return true;
                }

            }

            // Optionally, log the whole list
            Log.d(tag, "All Blocked Apps: " + blockedAppList);
            return false;
        } else {
            Log.d(tag, "No blocked apps found in SharedPreferences");
            return false;
        }
    }

}


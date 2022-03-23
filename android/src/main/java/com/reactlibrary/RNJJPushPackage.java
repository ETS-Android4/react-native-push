// ReactNativePushPackage.java

package com.reactlibrary;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

public class RNJJPushPackage implements ReactPackage {
    public static ReactApplicationContext reactContext;
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
        return Arrays.<NativeModule>asList(new RNJJPushModule(reactContext));
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}

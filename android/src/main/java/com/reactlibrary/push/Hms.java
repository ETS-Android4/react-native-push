package com.reactlibrary.push;

import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.facebook.react.bridge.ReactApplicationContext;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.common.ResolvableApiException;
import com.huawei.hms.push.HmsMessaging;
import com.huawei.hms.support.log.HMSLog;
import com.reactlibrary.IPush;
import com.reactlibrary.PushHelper;

public class Hms extends BasePush {
    private HmsMessaging hmsMessaging;
    public Hms(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public void init() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // 输入token标识"HCM"
                    String tokenScope = "HCM";
                    hmsMessaging = HmsMessaging.getInstance(context);
                    String token = HmsInstanceId.getInstance(context).getToken(String.valueOf(PushHelper.getConfigValue(context, "com.huawei.hms.client.appid")), tokenScope);
                    Log.i(TAG, "get token: " + token);
                    hmsMessaging.subscribe("all-member");
                } catch (ApiException e) {
                    // 应用拉起引导升级HMS Core（APK）页面方法
                    updateHMS(e);
                    Log.e(TAG, "get token failed, " + e);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void init(String appId, String appKey) {
        this.init();
    }

    @Override
    public void setAlias(String alias) {
        hmsMessaging.subscribe(alias);
    }

    @Override
    public void unsetAlias(String alias) {
        hmsMessaging.unsubscribe(alias);
    }

    @Override
    public void setTag(String tag) {
        hmsMessaging.subscribe(tag);
    }

    @Override
    public void unsetTag(String tag) {
        hmsMessaging.unsubscribe(tag);
    }

    private void updateHMS(ApiException e) {
        if (e instanceof ResolvableApiException) {
            PendingIntent resolution = ((ResolvableApiException) e).getResolution();
            if (resolution != null) {
                try {
                    HMSLog.e(HmsInstanceId.TAG, "has resolution by pendingIntent");
                    resolution.send();
                } catch (PendingIntent.CanceledException ex) {
                    Log.i(TAG, "Failed to resolve, " + e.getMessage());
                }
            } else {
                Intent resolutionIntent = ((ResolvableApiException) e).getResolutionIntent();
                if (resolutionIntent != null) {
                    HMSLog.e(HmsInstanceId.TAG, "has resolution by intent");
                    resolutionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(resolutionIntent);
                }
            }
        }
    }

}

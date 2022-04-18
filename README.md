# react-native-push

## Getting started

`$ npm install react-native-push --save`

### Mostly automatic installation

`$ react-native link react-native-push`

## Usage
```javascript
import JJPush from 'react-native-push';

// 用户同意协议之后初始化
JJPush.init(disable);

//example    禁用oppo推送
JJPush.init({oppo:true});

//本地通知
JJPush.notify(title, body)

// 设置别名
JJPush.setAlias(alias)

// 删除别名
JJPush.unsetAlias(alias)

// 设置标签
JJPush.setTag(tag)

// 删除标签绑定
JJPush.unsetTag(tag)


// 监听通知消息，oppo和vivo不支持
JJPush.addEventListener('jjpush_notify', (res) => {
    console.log("jjpush", res)
});

// 监听通知栏点击
JJPush.addEventListener('jjpush_click', (res) => {
    console.log("jjpush", res)
});

// ios，通过通知栏启动应用
JJPush.getInitialNotification((res) => {
    alert(JSON.stringify(res))
});
```


### 华为

https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/android-config-agc-0000001050170137
- 华为后台创建项目与应用
- 签名校验SHA1256
- 开通推送服务
```
// build.gradle
buildscript {
    repositories {
        google()
        // 配置HMS Core SDK的Maven仓地址。
        maven {url 'https://developer.huawei.com/repo/'}
    }
}
```
```xml

<!--AndroidManifest.xml-->
<meta-data
    android:name="com.huawei.hms.client.appid"
    android:value="appId"/>

```

### vivo
```xml
<!--AndroidManifest.xml-->

<meta-data
    android:name="com.vivo.push.api_key"
    android:value="xxxxxxxx"/>

<meta-data
    android:name="com.vivo.push.app_id"
    android:value="xxxx"/>
```

### 小米

#### ANDROID
```xml
<!--AndroidManifest.xml-->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.VIBRATE"/> 
<permission android:name="com.xiaomi.mipushdemo.permission.MIPUSH_RECEIVE"
android:protectionLevel="signature" /> <!--这里com.xiaomi.mipushdemo改成app的包名-->
<uses-permission android:name="com.xiaomi.mipushdemo.permission.MIPUSH_RECEIVE" /><!--这里com.xiaomi.mipushdemo改成app的包名-->


<meta-data
    android:name="com.xm.push.appid"
    android:value="xxxxxxxx"/>
<meta-data
    android:name="com.xm.push.appkey"
    android:value="xxxxxxxx"/>
```
#### IOS

- 引入库
> UserNotifications.framework(iOS10+), libresolv.dylib, libxml2.dylib, libz.dylib,SystemConfiguration.framework，MobileCoreServices.framework，CFNetwork.framework，CoreTelephony.framework
- `target`的`Capabilities`选项卡添加`Push Notifications`
- 在`Build Settings` 中的 `Other Linker Flags` 中增加 `-ObjC`
- 在info.plist中加入
- > MiSDKAppID {xxxxx}
- > MiSDKAppKey {xxxxxx}
- > MiSDKRun  {debug or online}

- 在AppDelegate.h中添加
```c
...
#import <React/RCTLinkingManager.h>
#import "RCTJJPushModule.h"

...
[RCTJJPushModule application:application didFinishLaunchingWithOptions:launchOptions];
...

...

- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{

  [RCTJJPushModule application:application didRegisterUserNotificationSettings:notificationSettings];

}


- (void)application:(UIApplication *)app
        didFailToRegisterForRemoteNotificationsWithError:(NSError *)err
{
  NSLog(@"推送注册失败：%@", err);
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken
{
  [RCTJJPushModule application:application didRegisterForRemoteNotificationsWithDeviceToken:deviceToken];

}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)notification
{
  [RCTJJPushModule application:application didReceiveRemoteNotification:notification];

}

- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification {
  [RCTJJPushModule application:application didReceiveLocalNotification:notification];

}

// ios 10
// 应用在前台收到通知
- (void)userNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
  [RCTJJPushModule userNotificationCenter:center willPresentNotification:notification withCompletionHandler:completionHandler];
}

// 点击通知进入应用
- (void)userNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler {
  [RCTJJPushModule userNotificationCenter:center didReceiveNotificationResponse:response withCompletionHandler:completionHandler];
  completionHandler();
}

//#define __IPHONE_10_0    100000
- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey, id> *)options
{
  //6.3的新的API调用，是为了兼容国外平台(例如:新版facebookSDK,VK等)的调用[如果用6.2的api调用会没有回调],对国内平台没有影响。
  
    return [RCTLinkingManager application:app openURL:url options:options];
}


- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void(^)(NSArray<id<UIUserActivityRestoring>> * __nullable restorableObjects))restorationHandler {
  
    return [RCTLinkingManager application:application
    continueUserActivity:userActivity
                       restorationHandler:restorationHandler];
}

```

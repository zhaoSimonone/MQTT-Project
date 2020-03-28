### Android端连接云需要导入的库或者依赖
[Eclipse Paho Android Service](https://github.com/eclipse/paho.mqtt.android)
#### Android Studio需要导入的依赖
```
implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0'
implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
```
#### Android Studio需要开启的权限
```
<uses-permission android:name="android.permission.INTERNET"/>
```


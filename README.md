# BmobTestDemo
云数据库---Bmob使用

注册账号、创建应用
https://docs.bmob.cn/data/Android/a_faststart/doc/index.html

AS集成
SDK导入
AndroidStudio配置
鉴于目前Google官方推荐使用 Android Studio 进行Android项目开发，自 V3.4.2 开始，Bmob Android SDK 可以使用Gradle来进行包依赖管理，如果你使用Android Studio来进行基于BmobSDK的项目开发，有两种方式：

自动导入(推荐)
请按照如下两个步骤进行：

在 Project 的 build.gradle 文件中添加 Bmob的maven仓库地址，示例如下：（注意文字说明部分）：

 buildscript {
     repositories {
         jcenter()
     }
     dependencies {
         classpath     'com.android.tools.build:gradle:1.2.3'
     }
 }

 allprojects {
     repositories {
         jcenter()
         //Bmob的maven仓库地址--必填
         maven { url "https://raw.github.com/bmob/bmob-android-sdk/master" }
     }
 }
在app的build.gradle文件中添加compile依赖文件,示例如下：（注意文字说明部分）：

apply plugin: 'com.android.application'

android {
    compileSdkVersion 22
    buildToolsVersion '22.0.1'

    **兼容Android6.0系统所需，如果这句话报错，可在dependencies标签下使用compile 'cn.bmob.android:http-legacy:1.0'**
    useLibrary 'org.apache.http.legacy'

    ...
}
    dependencies {
        compile fileTree(dir: 'libs', include: ['*.jar'])

        //以下SDK开发者请根据需要自行选择
        //bmob-sdk：Bmob的android sdk包，包含了Bmob的数据存储、文件等服务，以下是最新的bmob-sdk:
        //3.5.5：请务必查看下面注释[1]
        compile 'cn.bmob.android:bmob-sdk:3.5.5'

        //bmob-push：Bmob的推送包
        compile 'cn.bmob.android:bmob-push:0.8'

        //bmob-im：Bmob的即时通讯包，注意每个版本的im依赖特定版本的bmob-sdk，具体的依赖关系可查看下面注释[2]
        compile 'cn.bmob.android:bmob-im:2.0.5@aar'
        compile 'cn.bmob.android:bmob-sdk:3.4.7-aar'

        //bmob-sms ：Bmob单独为短信服务提供的包
        compile 'cn.bmob.android:bmob-sms:1.0.1'

        //如果你想应用能够兼容Android6.0，请添加此依赖(org.apache.http.legacy.jar)
        compile 'cn.bmob.android:http-legacy:1.0'
    }
    
    
    配置AndroidManifest.xml
在你的应用程序的AndroidManifest.xml文件中添加相应的权限：


<!--允许联网 --> 
<uses-permission android:name="android.permission.INTERNET" /> 
<!--获取GSM（2g）、WCDMA（联通3g）等网络状态的信息  --> 
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
<!--获取wifi网络状态的信息 --> 
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> 
<!--保持CPU 运转，屏幕和键盘灯有可能是关闭的,用于文件上传和下载 -->
<uses-permission android:name="android.permission.WAKE_LOCK" /> 
<!--获取sd卡写的权限，用于文件上传和下载-->
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<!--允许读取手机状态 用于创建BmobInstallation--> 
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="cn.bmob.example"
        android:versionCode="1"
        android:versionName="1.0">

    <uses-sdk android:minSdkVersion="8" android:targetSdkVersion="17"/>

    <uses-permission android:name="android.permission.INTERNET" /> 
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" /> 
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" /> 
    <uses-permission android:name="android.permission.WAKE_LOCK" /> 
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />

    <application
        android:allowBackup="true"
        android:icon="@drawable/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">
        <activity
            android:name="cn.bmob.example.MainActivity"
            android:screenOrientation="portrait"
            android:label="@string/app_name">

                <action android:name="android.intent.action.MAIN"/>
                <category android:name="android.intent.category.LAUNCHER"/>


        <activity
            android:name=".CreateActivity"
            android:screenOrientation="portrait">
        <activity
            android:name=".DeleteActivity"
            android:screenOrientation="portrait">
        <activity
            android:name=".UpdateActivity"
            android:screenOrientation="portrait">
        <activity
            android:name=".FindActivity"
            android:screenOrientation="portrait">
    </application>
</manifest>
初始化BmobSDK
在你应用程序启动的Activity的onCreate()方法中初始化Bmob功能。代码如下所示：

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
         // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
         //提供以下两种方式进行初始化操作：

        //第一：默认初始化
        Bmob.initialize(this, "Your Application ID");
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        //Bmob.initialize(this, "Your Application ID","bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
    }
}



添加一行数据
首先创建JavaBean（对应为Bmob后台的数据表，更详细的解释请查看Android开发文档）

public class Person extends BmobObject {
    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
添加数据

Person p2 = new Person();
p2.setName("lucky");
p2.setAddress("北京海淀");
p2.save(new SaveListener<String>() {
    @Override
    public void done(String objectId,BmobException e) {
        if(e==null){
            toast("添加数据成功，返回objectId为："+objectId);
        }else{
            toast("创建数据失败：" + e.getMessage());
        }
    }
});
如果toast出添加数据成功的消息，你会在Bmob对应Application Id的数据表中看到有一行新增的数据，如下图所示：



获取一行数据
//查找Person表里面id为6b6c11c537的数据
BmobQuery<Person> bmobQuery = new BmobQuery<Person>();
bmobQuery.getObject("6b6c11c537", new >QueryListener<Person>() {
    @Override
    public void done(Person object,BmobException e) {
        if(e==null){
            toast("查询成功");
        }else{
            toast("查询失败：" + e.getMessage());
        }
    }
});
修改一行数据
//更新Person表里面id为6b6c11c537的数据，address内容更新为“北京朝阳”
Person p2 = new Person();
p2.setAddress("北京朝阳");
p2.update("6b6c11c537", new UpdateListener() {

    @Override
    public void done(BmobException e) {
        if(e==null){
            toast("更新成功:"+p2.getUpdatedAt());
        }else{
            toast("更新失败：" + e.getMessage());
        }
    }

});
删除一行数据
Person p2 = new Person();
p2.setObjectId("6b6c11c537");
p2.delete(new UpdateListener() {

    @Override
    public void done(BmobException e) {
        if(e==null){
            toast("删除成功:"+p2.getUpdatedAt());
        }else{
            toast("删除失败：" + e.getMessage());
        }
    }

});


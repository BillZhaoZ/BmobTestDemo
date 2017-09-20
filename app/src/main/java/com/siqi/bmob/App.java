package com.siqi.bmob;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created on Bill 17/9/20
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * 初始化日志工具
         */
        Logger.addLogAdapter(new AndroidLogAdapter());

        //TODO 集成：1.4、初始化数据服务SDK、保存设备信息并启动推送服务
        /**
         * 初始化比目数据SDK
         */
        Bmob.initialize(this, "d0ba8c76bb972922db55031b5fbbd18f");

        /**
         * 保存设备信息，用于推送功能
         */
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    Logger.i("初始化成功：" + bmobInstallation.getObjectId() + "---" + bmobInstallation.getInstallationId());
                } else {
                    Logger.e("初始化失败：" + e.getMessage());
                }
            }
        });

        /**
         * 启动推送服务
         */
        BmobPush.startWork(this);

    }
}

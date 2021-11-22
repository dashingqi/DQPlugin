package com.dashingqi.dqplugin

import android.app.Application

/**
 * @author zhangqi61
 * @since 2021/11/22
 */
class PluginApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LoadClassUtil.loadClass(this,"sdcard/loadplugin.apk")
    }
}
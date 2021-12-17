package com.dashingqi.dqplugin

import android.annotation.SuppressLint

/**
 * @desc : Hook 工具类
 * @author : zhangqi
 * @time : 2021/12/3 22:57
 */
object HookUtil {

    /**
     * hook ams
     */
    @SuppressLint("DiscouragedPrivateApi", "PrivateApi")
    fun hookAMS() {

        // 获取到ActivityTaskManager的类对象
        val activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager")
        // 获取到Singleton
        val singletonFiled = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton")


    }

}
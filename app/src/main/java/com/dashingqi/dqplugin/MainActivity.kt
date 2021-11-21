package com.dashingqi.dqplugin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import dalvik.system.DexClassLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            // 定义一个DexClassLoader
            val dexClassLoader = DexClassLoader(
                "/sdcard/Dashingqi.dex",
                this.cacheDir.absolutePath,
                null, classLoader
            )
            val loadClass = dexClassLoader.loadClass("com.dashingqi.plugin.DQPlugin")
            val printMethod = loadClass.getMethod("println")
            printMethod.invoke(loadClass)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
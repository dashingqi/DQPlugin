package com.dashingqi.dqplugin

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.dashingqi.plugin.DQPlugin
import dalvik.system.DexClassLoader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity classLoader is $classLoader")

        val parentClassLoader = classLoader.parent

        Log.d(TAG, "parent classloader is $parentClassLoader")

        Log.d(TAG, "Activity classloader is ${Activity::class.java.classLoader}")

        DQPlugin.print()

        try {
            // 定义一个DexClassLoader
            val dexClassLoader = DexClassLoader(
                "/sdcard/Dashingqi.dex",
                this.cacheDir.absolutePath,
                null, classLoader
            )

            val dexClassLoaderParent = dexClassLoader.parent
            Log.d(TAG, "DexClassLoader parent is $dexClassLoaderParent")

            val loadClass = dexClassLoader.loadClass("com.dashingqi.plugin.DQPlugin")
            val printMethod = loadClass.getMethod("println")
            printMethod.invoke(loadClass)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }

}
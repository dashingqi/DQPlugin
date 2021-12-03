package com.dashingqi.dqplugin

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import com.dashingqi.plugin.DQPlugin
import dalvik.system.DexClassLoader

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity classLoader is $classLoader")
        findViewById<TextView>(R.id.tvJump).setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        // 系统出厂时间
        val time = Build.TIME
        Log.d(TAG, "onCreate: time == $time")

        val parentClassLoader = classLoader.parent

        Log.d(TAG, "parent classloader is $parentClassLoader")

        Log.d(TAG, "Activity classloader is ${Activity::class.java.classLoader}")

        try {
            // 定义一个DexClassLoader
            val dexClassLoader = DexClassLoader(
                "/sdcard/DQPlugin.dex",
                this.cacheDir.absolutePath,
                null, classLoader
            )

            val dexClassLoaderParent = dexClassLoader.parent
            Log.d(TAG, "DexClassLoader parent is $dexClassLoaderParent")

            val loadClass = dexClassLoader.loadClass("com.dashingqi.plugin.DQPlugin")
            val printMethod = loadClass.getMethod("print")

            val logMethod = loadClass.getMethod("log")
            printMethod.invoke(null)
            // 通过类对象获取到类的对象
            val loadClassInstance = loadClass.newInstance()
            logMethod.invoke(loadClassInstance)

            val loadPluginClass = Class.forName("com.dashingqi.loadplugin.LoadPlugin")
            Log.d(TAG, "loadPlugin classLoader is ${loadPluginClass.classLoader}")
            val loadPluginInstance = loadPluginClass.newInstance()
            val performClickMethod = loadPluginClass.getMethod("performClick")
            performClickMethod.invoke(loadPluginInstance)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }

}
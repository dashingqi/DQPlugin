package com.dashingqi.dqplugin

import android.content.Context
import dalvik.system.DexClassLoader
import java.lang.Exception

/**
 * @author zhangqi61
 * @since 2021/11/22
 */
object LoadClassUtil {

    /**
     * 加载class 将宿主的dexElements + 插件的dexElements 进行合并 --> 新的宿主DexElements
     * @param context Context 上下文环境
     * @param pluginApk String 存放插件APK路径
     */
    fun loadClass(context: Context, pluginApk: String) {
        context ?: return
        pluginApk ?: return
        try {
            // 获取到BaseDexClassLoader 类对象
            val baseDexClassLoaderClass = Class.forName("dalvik.system.BaseDexClassLoader")
            val pathListField = baseDexClassLoaderClass.getDeclaredField("pathList")
            pathListField.isAccessible = true

            // 获取到DexPathList 类对象
            val dexPathListClass = Class.forName("dalvik.system.DexPathList")
            val dexElementsField = dexPathListClass.getDeclaredField("dexElements")
            dexElementsField.isAccessible = true

            //宿主的ClassLoader
            val hostClassLoader = context.classLoader
            // 宿主的DexPathList对象 get() --> 该变量存在于那个类中 就传入该类的对象 就能获取该引用指向的对象
            val hostPathListInstance = pathListField.get(hostClassLoader)
            // 宿主的DexElements对象
            val hostDexElements = dexElementsField.get(hostPathListInstance) as Array<Any>


            //插件的ClassLoader
            val pluginDexClassLoader =
                DexClassLoader(pluginApk, context.cacheDir.absolutePath, null, hostClassLoader)
            // 插件的DexPathList对象
            val pluginDexPathListInstance = pathListField.get(pluginDexClassLoader)
            // 插件的DexElements对象
            val pluginDexElements = dexElementsField.get(pluginDexPathListInstance) as Array<Any>

            // 创建一个新的数组
            val newDexElementArray = java.lang.reflect.Array.newInstance(
                hostDexElements::class.java.componentType, hostDexElements.size +
                        pluginDexElements.size
            )

            // 复制数组
            System.arraycopy(hostDexElements, 0, newDexElementArray, 0, hostDexElements.size)
            System.arraycopy(pluginDexElements, 0, newDexElementArray, hostDexElements.size, hostDexElements.size)

            // 赋值
            dexElementsField.set(hostPathListInstance, newDexElementArray)

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}
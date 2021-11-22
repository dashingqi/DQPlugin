# DQPlugin

## 插件化
#### 加载插件中的类
###### dex文件生成命令
``` shell
dx --dex --output=output.dex input.class
```
###### PathClassLoader
- ClassLoader parent 是 BootClassLoader
###### DexClassLoader
- DexClassLoader parent 是 PathClassLoader

###### 宿主与插件进行合并---> DexElements
- 需要手动给应用权限
```kotlin
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

            // 赋值 （新的DexElements赋值给宿主原本的DexElements）
            dexElementsField.set(hostPathListInstance, newDexElementArray)

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
```
- 合并插件后，可以直接去反射插件中的类
```kotlin
val loadPluginClass:Class<*>? = Class.forName("com.dashingqi.loadplugin.LoadPlugin")
// 这个ClassLoader是PathClassLoader
Log.d(TAG, "loadPlugin classLoader is ${loadPluginClass.classLoader}")
```

#### 加载插件中的四大组件
###### MainActivity
- MainActivity的ClassLoader是PathClassLoader
###### Activity
- Activity的ClassLoader的BootClassLoader

#### 加载插件中的资源

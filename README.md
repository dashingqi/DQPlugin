# DQPlugin

## 插件化
#### 加载插件中的类
###### PathClassLoader
- ClassLoader parent 是 BootClassLoader
###### DexClassLoader
- DexClassLoader parent 是 PathClassLoader

#### 加载插件中的四大组件
###### MainActivity
- MainActivity的ClassLoader是PathClassLoader
###### Activity
- Activity的ClassLoader的BootClassLoader

#### 加载插件中的资源

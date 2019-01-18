# AndroidUVCCamera
Android外接UVC相机库，支持预览和拍照。
[中文文档：最好用的Android UVC Camera库](https://blog.csdn.net/u011630465/article/details/86511258)
# How to
Step 1. Add the JitPack repository to your build file.Add it in your root build.gradle at the end of repositories:
```java
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Step 2. Add the dependency
```java
dependencies {
        implementation 'com.github.Liuguihong:AndroidUVCCamera:1.0.0'
}
```

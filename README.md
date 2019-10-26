# AndroidUVCCamera
Android外接UVC相机库，致力于使用最简单的API、最清晰的代码逻辑实现预览和拍照功能。
[中文文档：最好用的Android UVC Camera库](https://blog.csdn.net/u011630465/article/details/86511258)

最近在做一个外接USB相机的项目，github上搜了下，有很多开源的库，这些库底层基本用的都是同一套东西，但上层业务比较繁琐，使用起来很不方便，并且也不太符合项目的业务需求，所以重新封装了一下，本库只需几个简单的API即可完成预览、拍照功能，无需关注复杂的USB插拔处理逻辑。
#### 1.添加依赖
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
#### 2.创建UVCCameraProxy对象
```javascript
UVCCameraProxy mUVCCamera = new UVCCameraProxy(this);
```
#### 3.添加配置（可选）
```javascript
mUVCCamera.getConfig()
         .isDebug(true) // 是否调试
         .setPicturePath(PicturePath.APPCACHE) // 图片保存路径，保存在app缓存还是sd卡
         .setDirName("uvccamera") // 图片保存目录名称
         .setProductId(0) // 产品id，用于过滤设备，不需要可不设置
         .setVendorId(0); // 供应商id，用于过滤设备，不需要可不设置
```
#### 4.设置预览View
支持TextureView和SurfaceView，在其生命周期里封装了注册/注销USB插拔广播、释放相机资源等逻辑，并且自动过滤USB设备，只响应USB相机插拔。
```javascript
mUVCCamera.setPreviewTexture(mTextureView); // TextureView
// mUVCCamera.setPreviewSurface(mSurfaceView); // SurfaceView
```
#### 5.设置USB监听回调，并在回调里处理相关逻辑
```javascript
mUVCCamera.setConnectCallback(new ConnectCallback() {
    @Override
    public void onAttached(UsbDevice usbDevice) {
        mUVCCamera.requestPermission(usbDevice); // USB设备授权
    }

    @Override
    public void onGranted(UsbDevice usbDevice, boolean granted) {
        if (granted) {
            mUVCCamera.connectDevice(usbDevice); // 连接USB设备
        }
    }

    @Override
    public void onConnected(UsbDevice usbDevice) {
        mUVCCamera.openCamera(); // 打开相机
    }

    @Override
    public void onCameraOpened() {
        mUVCCamera.setPreviewSize(640, 480); // 设置预览尺寸
        mUVCCamera.startPreview(); // 开始预览
    }

    @Override
    public void onDetached(UsbDevice usbDevice) {
        mUVCCamera.closeCamera(); // 关闭相机
    }
});
```
#### 6.设置拍照按钮点击回调（可选）
```javascript
mUVCCamera.setPhotographCallback(new PhotographCallback() {
    @Override
    public void onPhotographClick() {
        mUVCCamera.takePicture();
    }
});
```
#### 7.设置预览回调（可选）
```javascript
mUVCCamera.setPreviewCallback(new PreviewCallback() {
    @Override
    public void onPreviewFrame(byte[] yuv) {

    }
});
```
#### 8.设置拍照成功图片回调（可选）
```javascript
mUVCCamera.setPictureTakenCallback(new PictureCallback() {
    @Override
    public void onPictureTaken(String path) {
    
    }
});
```
#### 9.拍照
```javascript
mUVCCamera.takePicture();
// mUVCCamera.takePicture("test.jpg"); // 自定义图片名称，不设置则根据UUID自动保存
```
#### 10.其他API
|方法|说明|
|:-------------|:-------------|
|registerReceiver()|注册USB插拔监听广播|
|unregisterReceiver()|注销USB插拔监听广播|
|checkDevice()|查找USB相机设备，会在onAttached里回调|
|requestPermission(UsbDevice usbDevice)|USB设备授权，要连接USB设备必须先授权|
|connectDevice(UsbDevice usbDevice)|连接USB设备|
|closeDevice()|关闭USB设备|
|openCamera()|打开相机|
|closeCamera()|关闭相机|
|setPreviewSurface(SurfaceView surfaceView)|设置预览View为SurfaceView|
|setPreviewTexture(TextureView textureView)|设置预览View为TextureView|
|setPreviewDisplay(Surface surface)|设置预览View，自定义|
|setPreviewRotation(float rotation)|设置相机预览旋转角度，暂时只支持TextureView|
|setPreviewSize(int width, int height)|设置预览尺寸|
|getPreviewSize()|获取相机预览尺寸|
|getSupportedPreviewSizes()|获取相机支持的预览尺寸|
|startPreview()|开始预览|
|stopPreview()|停止预览|
|takePicture()|拍照|
|takePicture(String pictureName)|拍照|
|isCameraOpen()|是否已经打开相机|
|getConfig()|获取配置信息|
|clearCache()|删除图片缓存目录|
#### 参考
 [https://github.com/saki4510t/UVCCamera](https://github.com/saki4510t/UVCCamera)


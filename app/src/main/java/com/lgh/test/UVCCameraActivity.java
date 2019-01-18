package com.lgh.test;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lgh.uvccamera.UVCCameraProxy;
import com.lgh.uvccamera.bean.PicturePath;
import com.lgh.uvccamera.callback.ConnectCallback;
import com.lgh.uvccamera.callback.PhotographCallback;
import com.lgh.uvccamera.callback.PictureCallback;
import com.lgh.uvccamera.callback.PreviewCallback;
import com.serenegiant.usb.Size;

import java.util.ArrayList;
import java.util.List;

public class UVCCameraActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextureView mTextureView;
    //    private SurfaceView mSurfaceView;
    private ImageView mImageView1;
    private Spinner mSpinner;
    private UVCCameraProxy mUVCCamera;
    private boolean isFirst = true;
    private String path1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvc_camera);
        initView();
        initUVCCamera();
    }

    private void initView() {
        mTextureView = findViewById(R.id.textureView);
//        mSurfaceView = findViewById(R.id.surfaceView);
//        mSurfaceView.setZOrderOnTop(true);
        mImageView1 = findViewById(R.id.imag1);
        mSpinner = findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if (isFirst) {
                    isFirst = false;
                    return;
                }
                Log.i(TAG, "position-->" + position);
                mUVCCamera.stopPreview();
                List<Size> list = mUVCCamera.getSupportedPreviewSizes();
                if (!list.isEmpty()) {
                    mUVCCamera.setPreviewSize(list.get(position).width, list.get(position).height);
                    mUVCCamera.startPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initUVCCamera() {
        mUVCCamera = new UVCCameraProxy(this);
        // 已有默认配置，不需要可以不设置
        mUVCCamera.getConfig()
                .isDebug(true)
                .setPicturePath(PicturePath.APPCACHE)
                .setDirName("uvccamera")
                .setProductId(0)
                .setVendorId(0);
        mUVCCamera.setPreviewTexture(mTextureView);
//        mUVCCamera.setPreviewSurface(mSurfaceView);
//        mUVCCamera.registerReceiver();

        mUVCCamera.setConnectCallback(new ConnectCallback() {
            @Override
            public void onAttached(UsbDevice usbDevice) {
                mUVCCamera.requestPermission(usbDevice);
            }

            @Override
            public void onGranted(UsbDevice usbDevice, boolean granted) {
                if (granted) {
                    mUVCCamera.connectDevice(usbDevice);
                }
            }

            @Override
            public void onConnected(UsbDevice usbDevice) {
                mUVCCamera.openCamera();
            }

            @Override
            public void onCameraOpened() {
                showAllPreviewSizes();
                mUVCCamera.setPreviewSize(640, 480);
                mUVCCamera.startPreview();
            }

            @Override
            public void onDetached(UsbDevice usbDevice) {
                mUVCCamera.closeCamera();
            }
        });

        mUVCCamera.setPhotographCallback(new PhotographCallback() {
            @Override
            public void onPhotographClick() {
                mUVCCamera.takePicture();
//                mUVCCamera.takePicture("test.jpg");
            }
        });

        mUVCCamera.setPreviewCallback(new PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] yuv) {

            }
        });

        mUVCCamera.setPictureTakenCallback(new PictureCallback() {
            @Override
            public void onPictureTaken(String path) {
                path1 = path;
                mImageView1.setImageURI(null);
                mImageView1.setImageURI(Uri.parse(path));
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                mUVCCamera.startPreview();
                break;

            case R.id.btn2:
                mUVCCamera.stopPreview();
                break;

            case R.id.btn3:
                mUVCCamera.clearCache();
                break;

            case R.id.btn4:
                mUVCCamera.setPreviewRotation(180);
                break;

            case R.id.take_picture:
//                mUVCCamera.takePicture();
                mUVCCamera.takePicture("test.jpg");
                break;

            case R.id.imag1:
                Log.i(TAG, "path1-->" + path1);
                jump2ImageActivity(path1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mUVCCamera.unregisterReceiver();
    }

    private void jump2ImageActivity(String path) {
        Intent intent = new Intent(this, ImageActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }

    private void showAllPreviewSizes() {
        isFirst = true;
        List<Size> previewList = mUVCCamera.getSupportedPreviewSizes();
        List<String> previewStrs = new ArrayList<>();
        for (Size size : previewList) {
            previewStrs.add(size.width + " * " + size.height);
        }
        ArrayAdapter adapter = new ArrayAdapter(UVCCameraActivity.this, android.R.layout.simple_spinner_item, previewStrs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

}

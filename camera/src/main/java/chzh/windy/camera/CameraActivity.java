package chzh.windy.camera;

import android.hardware.Camera;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import chzh.windy.camera.app.App;
import chzh.windy.camera.app.BaseCameraActivity;
import chzh.windy.camera.util.CameraUtils;
import chzh.windy.camera.view.CameraPreview;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Camera API. Android KitKat 及以前版本的 Android 使用 Camera API.
 */
@SuppressWarnings("deprecation")
public class CameraActivity extends BaseCameraActivity {

    FrameLayout mFlCameraPreview;
    ImageView mIvCameraButton;
    TextView mTvCameraHint;
    ImageView mFlashBtn;//闪光灯
    ImageView mChangeBtn;//切换摄像头
    TextView mCancelBtn;//取消摄像

    File mFile;
    Camera mCamera;
    CameraPreview mPreview;
    long mMaxPicturePixels;

    /**
     * 预览的最佳尺寸是否已找到
     */
    volatile boolean mPreviewBestFound;

    /**
     * 拍照的最佳尺寸是否已找到
     */
    volatile boolean mPictureBestFound;

    /**
     * finish()是否已调用过
     */
    volatile boolean mFinishCalled;


    @Override
    protected int getContentViewResId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_camera;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_camera);
        mFlCameraPreview = (FrameLayout) findViewById(R.id.fl_camera_preview);
        mIvCameraButton = (ImageView) findViewById(R.id.iv_camera_button);
        mTvCameraHint = (TextView) findViewById(R.id.tv_camera_hint);
        mFlashBtn = (ImageView) findViewById(R.id.camera_flash_btn);
        mFlashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlashMode();
            }
        });
        mChangeBtn = (ImageView) findViewById(R.id.iv_change_camera);
        mChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCamera();
            }
        });
        mCancelBtn = (TextView) findViewById(R.id.tv_cancel);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        int bg = getIntent().getIntExtra("bg", -1);
        if (bg != -1 ){
            ImageView bgView = (ImageView) findViewById(R.id.id_background);
            bgView.setBackgroundDrawable(getResources().getDrawable(bg));
        }
    }


    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void preInitData() {
        mFile = new File(getIntent().getStringExtra("file"));
        mTvCameraHint.setText(getIntent().getStringExtra("hint"));
        mMaxPicturePixels = getIntent().getIntExtra("maxPicturePixels", 3840 * 2160);
        initCamera();
        RxView.clicks(mIvCameraButton)
                //防止手抖连续多次点击造成错误
                .throttleFirst(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    if (mCamera == null)
                        return;
                    mCamera.takePicture(null, null, (data, camera) -> Observable
                            .create((Observable.OnSubscribe<Integer>) subscriber -> {
                                try {
                                    if (mFile.exists())
                                        mFile.delete();
                                    FileOutputStream fos = new FileOutputStream(mFile);
                                    fos.write(data);
                                    try {
                                        fos.close();
                                    } catch (Exception ignored) {
                                    }
                                    subscriber.onNext(200);
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(integer -> {
                                setResult(integer, getIntent().putExtra("file", mFile.toString()));
                                mFinishCalled = true;
                                finish();
                            }, throwable -> {
                                throwable.printStackTrace();
                                mCamera.startPreview();
                            }));
                });
    }

    void initCamera() {
        Observable.create((Observable.OnSubscribe<Camera>) subscriber -> subscriber.onNext(CameraUtils
                .getCamera()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(camera -> {
                    if (camera == null) {
                        Toast.makeText(App.sApp, "相机开启失败，再试一次吧", Toast.LENGTH_LONG).show();
                        mFinishCalled = true;
                        finish();
                        return;
                    }
                    mCamera = camera;
                    mPreview = new CameraPreview(CameraActivity.this, mCamera, (throwable, showToast) -> {
                        if (showToast)
                            Toast.makeText(App.sApp, "开启相机预览失败，再试一次吧", Toast.LENGTH_LONG).show();
                        mFinishCalled = true;
                        finish();
                    });
                    mFlCameraPreview.addView(mPreview);
                    initParams();
                });
    }

    void initParams() {
        Camera.Parameters params = mCamera.getParameters();
        //若相机支持自动开启/关闭闪光灯，则使用. 否则闪光灯总是关闭的.
        List<String> flashModes = params.getSupportedFlashModes();
        if (flashModes != null && flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)){
            params.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            light_num = 2;
//            flashMode = flashMode_auto;
//            setFlashMode();
        }else{
            light_num = 0;
            Camera.Parameters parameters = mCamera.getParameters();
            mFlashBtn.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_camera_flash_on));
            //                mFlashBtn.setBackgroundResource(R.mipmap.btn_camera_flash_on);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
            mCamera.setParameters(parameters);
        }
        mPreviewBestFound = false;
        mPictureBestFound = false;
        //寻找最佳预览尺寸，即满足16:9比例，且不超过1920x1080的最大尺寸;若找不到，则使用满足16:9的最大尺寸.
        //若仍找不到，使用最大尺寸;详见CameraUtils.findBestSize方法.
        CameraUtils previewUtils = new CameraUtils();
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        previewUtils.findBestSize(false, previewSizes, previewUtils.new OnBestSizeFoundCallback() {
            @Override
            public void onBestSizeFound(Camera.Size size) {
                mPreviewBestFound = true;
                params.setPreviewSize(size.width, size.height);
                if (mPictureBestFound)
                    initFocusParams(params);
            }
        }, 1920 * 1080);
        //寻找最佳拍照尺寸，即满足16:9比例，且不超过maxPicturePixels指定的像素数的最大Size;若找不到，则使用满足16:9的最大尺寸.
        //若仍找不到，使用最大尺寸;详见CameraUtils.findBestSize方法.
        CameraUtils pictureUtils = new CameraUtils();
        List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
        pictureUtils.findBestSize(true, pictureSizes, pictureUtils.new OnBestSizeFoundCallback() {
            @Override
            public void onBestSizeFound(Camera.Size size) {
                mPictureBestFound = true;
                params.setPictureSize(size.width, size.height);
                if (mPreviewBestFound)
                    initFocusParams(params);
            }
        }, mMaxPicturePixels);
    }

    void initFocusParams(Camera.Parameters params) {
        //若支持连续对焦模式，则使用.
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            setParameters(params);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            //进到这里，说明不支持连续对焦模式，退回到点击屏幕进行一次自动对焦.
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            setParameters(params);
            //点击屏幕进行一次自动对焦.
            mFlCameraPreview.setOnClickListener(v -> CameraUtils.autoFocus(mCamera));
            //4秒后进行第一次自动对焦，之后每隔8秒进行一次自动对焦.
            Observable.timer(4, TimeUnit.SECONDS)
                    .flatMap(aLong -> {
                        CameraUtils.autoFocus(mCamera);
                        return Observable.interval(8, TimeUnit.SECONDS);
                    }).subscribe(aLong -> CameraUtils.autoFocus(mCamera));
        }
    }

    void setParameters(Camera.Parameters params) {
        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            //非常罕见的情况
            //个别机型在SupportPreviewSizes里汇报了支持某种预览尺寸，但实际是不支持的，设置进去就会抛出RuntimeException.
            e.printStackTrace();
            try {
                //遇到上面所说的情况，只能设置一个最小的预览尺寸
                params.setPreviewSize(1920, 1080);
                mCamera.setParameters(params);
            } catch (Exception e1) {
                //到这里还有问题，就是拍照尺寸的锅了，同样只能设置一个最小的拍照尺寸
                e1.printStackTrace();
                try {
                    params.setPictureSize(1920, 1080);
                    mCamera.setParameters(params);
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        mFinishCalled = true;
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!mFinishCalled)
            finish();
    }

    //闪光灯模式 0:关闭 1: 开启 2: 自动
    private int light_num = 0;
    /**
     * 设置闪关灯开启模式
     */
    private void setFlashMode() {
        if(cameraPosition == 0){
            //前置
            Toast.makeText(this,"请切换为后置摄像头开启闪光灯",Toast.LENGTH_LONG).show();
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        switch (light_num) {

            case 0:
                //打开
                light_num = 1;
                mFlashBtn.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_camera_flash_on));
//                mFlashBtn.setBackgroundResource(R.mipmap.btn_camera_flash_on);
//                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);//开启
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);//开启
                mCamera.setParameters(parameters);
                break;
            case 1:
                //自动
                light_num = 2;
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                mCamera.setParameters(parameters);
//                mFlashBtn.setBackgroundResource(R.mipmap.btn_camera_flash_auto);
                mFlashBtn.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_camera_flash_auto));
                break;
            case 2:
                //关闭
                light_num = 0;
                //关闭
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
//                mFlashBtn.setBackgroundResource(R.mipmap.btn_camera_flash_off);
                mFlashBtn.setBackgroundDrawable(getResources().getDrawable(R.mipmap.btn_camera_flash_off));
                break;
        }
    }

    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
    /**
     * 切换摄像头
     */
    public void switchCamera() {
        releaseCamera();
        //切换前后摄像头
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数

        for(int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
            if(cameraPosition == 1) {
                //现在是后置，变更为前置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    releaseCamera();
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(mPreview.getHolder());//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 0;
                    break;
                }
            } else {
                //现在是前置， 变更为后置
                if(cameraInfo.facing  == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
                    releaseCamera();
                    mCamera = Camera.open(i);//打开当前选中的摄像头
                    try {
                        mCamera.setPreviewDisplay(mPreview.getHolder());//通过surfaceview显示取景画面
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    mCamera.startPreview();//开始预览
                    cameraPosition = 1;
                    break;
                }
            }

        }
//        preInitData();
    }
    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();//停掉原来摄像头的预览
            mCamera.release();//释放资源
            mCamera = null;//取消原来摄像头
        }
    }
}

package chzh.windy.camera.app;

import android.content.Context;
import android.os.Handler;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;

public class App {

    /**
     * 启动照相Intent的RequestCode.自定义相机.
     */
    public static final int TAKE_PHOTO_CUSTOM = 100;
    /**
     * 启动照相Intent的RequestCode.系统相机.
     */
    public static final int TAKE_PHOTO_SYSTEM = 200;
    /**
     * 主线程Handler.
     */
    public static Handler mHandler;
    public static Context sApp;

    public static void initCamera(Context context) {
        App.sApp = context;
        mHandler = new Handler();
        Fresco.initialize(context, ImagePipelineConfig
                .newBuilder(context)
                .setDownsampleEnabled(true)
                .build());
    }
}

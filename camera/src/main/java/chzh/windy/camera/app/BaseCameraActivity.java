package chzh.windy.camera.app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class BaseCameraActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        initView();
        preInitData();
    }

    protected abstract void initView();
    protected abstract int getContentViewResId();

    protected abstract void preInitData();

}

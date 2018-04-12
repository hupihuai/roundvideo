package com.weihu.roundvideo;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

public class PictureActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurfaceView;
    private PictureRenderer mPictureRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        mGLSurfaceView = findViewById(R.id.glSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mPictureRenderer = new PictureRenderer(mGLSurfaceView, this);
        mGLSurfaceView.setRenderer(mPictureRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        ((RadioGroup) findViewById(R.id.shapeRadioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.circleRb) {
                    mPictureRenderer.showCircle();
                } else {
                    mPictureRenderer.showRound();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPictureRenderer.onDestory();
    }
}

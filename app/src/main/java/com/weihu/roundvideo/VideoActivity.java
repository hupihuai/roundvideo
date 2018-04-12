package com.weihu.roundvideo;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

public class VideoActivity extends AppCompatActivity {

    private GLSurfaceView mGLSurfaceView;
    private VideoRenderer mVideoRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mGLSurfaceView = findViewById(R.id.glSurfaceView);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mVideoRenderer = new VideoRenderer(mGLSurfaceView, this);
        mGLSurfaceView.setRenderer(mVideoRenderer);
        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        ((RadioGroup) findViewById(R.id.shapeRadioGroup)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.circleRb) {
                    mVideoRenderer.showCircle();
                } else {
                    mVideoRenderer.showRound();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoRenderer.onDestory();
    }
}

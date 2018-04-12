package com.weihu.roundvideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VideoRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    private final GLSurfaceView mGLSurfaceView;
    private RoundShape mRoundShape;
    private BaseShape mCircleShape;
    private VideoTextture mVideoTextture;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];

    //video
    private MediaPlayer mediaPlayer;


    private static final int SHAPE_CIRCL = 0;
    private static final int SHAPE_ROUND = 1;
    private int currentShape = SHAPE_CIRCL;

    public VideoRenderer(GLSurfaceView glSurfaceView, Context context) {
        mGLSurfaceView = glSurfaceView;
        this.context = context;
        mRoundShape = new RoundShape(context.getResources());
        mCircleShape = new CircleShape(context.getResources());

        mVideoTextture = new VideoTextture(context.getResources());

        mediaPlayer = new MediaPlayer();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mRoundShape.create();
        mCircleShape.create();

        mVideoTextture.create();
        playVideo(mVideoTextture.getSurfaceTexture(), "android.resource://" + context.getPackageName() + "/" + R.raw.test);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        float ratio = (float) width / height;
//        Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -ratio, ratio, 3, 7);
        GLES20.glViewport(0, 0, width, height);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45, (float) width / height, 3, 7);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        mRoundShape.setMatrix(mMVPMatrix);
        mRoundShape.setSize(width, height);

        mCircleShape.setMatrix(mMVPMatrix);
        mCircleShape.setSize(width, height);


        mVideoTextture.setMatrix(mMVPMatrix);
        mVideoTextture.setSize(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glEnable(GLES20.GL_STENCIL_TEST);
        GLES20.glClear(GLES20.GL_STENCIL_BUFFER_BIT);
        GLES20.glStencilFunc(GLES20.GL_ALWAYS, 1, 0xff); // 总是通过
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_REPLACE);
        if (currentShape == SHAPE_CIRCL) {
            mCircleShape.draw();
        } else {
            mRoundShape.draw();
        }
        GLES20.glStencilFunc(GLES20.GL_EQUAL, 1, 0xff); // 只有模板缓冲区中的模板值为1的地方才被绘制
        GLES20.glStencilOp(GLES20.GL_KEEP, GLES20.GL_KEEP, GLES20.GL_KEEP);

        mVideoTextture.draw();
        GLES20.glDisable(GLES20.GL_STENCIL_TEST);


    }


    public void playVideo(SurfaceTexture surfaceTexture, String uri) {

        surfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                mVideoTextture.setUpdateSurface(true);
                mGLSurfaceView.requestRender();
            }
        });

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(uri));
            mediaPlayer.setLooping(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.setDataSource("http://cdnlab.makeblock.com/20171129_CodeyRocy_CN.mp4");
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            Surface surface = new Surface(surfaceTexture);
            mediaPlayer.setSurface(surface);
            surface.release();
            try {
                mediaPlayer.prepareAsync();
            } catch (Exception t) {
                Log.e("video ", "media player prepare failed");
            }

        } catch (
                Exception e)

        {
            e.printStackTrace();
        }

    }


    public void onDestory() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

    public void showCircle() {
        currentShape = SHAPE_CIRCL;
        mGLSurfaceView.requestRender();
    }

    public void showRound() {
        currentShape = SHAPE_ROUND;
        mGLSurfaceView.requestRender();
    }


}

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

public class PictureRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    private final GLSurfaceView mGLSurfaceView;
    private BaseShape mCircleShape;
    private RoundShape mRoundShape;
    private PictureTextture mPictureTextture;

    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16];

    private static final int SHAPE_CIRCL = 0;
    private static final int SHAPE_ROUND = 1;
    private int currentShape = SHAPE_CIRCL;

    public PictureRenderer(GLSurfaceView glSurfaceView, Context context) {
        mGLSurfaceView = glSurfaceView;
        this.context = context;
        mCircleShape = new CircleShape(context.getResources());
        mRoundShape = new RoundShape(context.getResources());

        mPictureTextture = new PictureTextture(context.getResources());

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mCircleShape.create();
        mRoundShape.create();
        mPictureTextture.create();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test);
        mPictureTextture.setBitmap(bitmap);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        float ratio = (float) width / height;
//        Matrix.frustumM(mProjectionMatrix, 0, -1, 1, -ratio, ratio, 3, 7);
        GLES20.glViewport(0, 0, width, height);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45, (float) width / height, 3, 7);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5, 0, 0, 0, 0, 1, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        mCircleShape.setMatrix(mMVPMatrix);
        mCircleShape.setSize(width, height);

        mRoundShape.setMatrix(mMVPMatrix);
        mRoundShape.setSize(width, height);

        mPictureTextture.setMatrix(mMVPMatrix);
        mPictureTextture.setSize(width, height);

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
        mPictureTextture.draw();
        GLES20.glDisable(GLES20.GL_STENCIL_TEST);


    }

    public void onDestory() {

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

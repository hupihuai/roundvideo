package com.weihu.roundvideo;

import android.content.res.Resources;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import javax.microedition.khronos.opengles.GL10;

public class VideoTextture extends BaseTextture {

    private SurfaceTexture mSurfaceTexture;


    private boolean mUpdateSurface;

    public VideoTextture(Resources res) {
        super(res);

    }


    @Override
    protected void createShader() {
//        super.createShader();
        createProgramByAssetsFile("shader/video_vertex.glsl",
                "shader/video_fragment.glsl");
    }

    @Override
    public void draw() {
        if (mUpdateSurface) {
            mSurfaceTexture.updateTexImage();
            mUpdateSurface = false;
        }
        super.draw();
    }

    @Override
    public void create() {
        super.create();
        int textureID = createTextureID();
        setTextureId(textureID);
        mSurfaceTexture = new SurfaceTexture(textureID);
    }

    private int createTextureID() {
        int[] texture = new int[1];
        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        return texture[0];
    }

    public SurfaceTexture getSurfaceTexture() {
        return mSurfaceTexture;
    }

    public void setUpdateSurface(boolean updateSurface) {
        mUpdateSurface = updateSurface;
    }
}

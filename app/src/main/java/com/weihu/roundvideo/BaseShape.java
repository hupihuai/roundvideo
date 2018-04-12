package com.weihu.roundvideo;

import android.content.res.Resources;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class BaseShape extends BaseProgram {

    public static final float[] OM = MatrixUtils.getOriginalMatrix();
    protected int mProgram;
    protected int mHPosition;
    protected int mHColor;
    /**
     * 总变换矩阵句柄
     */
    protected int mHMatrix;


    /**
     * 顶点坐标Buffer
     */
    protected FloatBuffer mVerBuffer;


    private float[] matrix = Arrays.copyOf(OM, 16);

    //顶点坐标
    protected float pos[] = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f,
    };

    protected float color[] = {0.0f, 1.0f, 0f, 1f};

    public BaseShape(Resources mRes) {
        super(mRes);
    }

    @Override
    public void create() {
        initBuffer();
        createProgramByAssetsFile("shader/base_shape_vertex.glsl",
                "shader/base_shape_fragment.glsl");
    }

    @Override
    public void setSize(int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void draw() {
        onClear();
        onUseProgram();
        onSetExpandData();
        onDraw();
    }

    public void setMatrix(float[] matrix) {
        this.matrix = Arrays.copyOf(matrix, 16);
    }

    public float[] getMatrix() {
        return matrix;
    }


    protected final void createProgram(String vertex, String fragment) {
        mProgram = uCreateGlProgram(vertex, fragment);
        mHPosition = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHColor = GLES20.glGetUniformLocation(mProgram, "vColor");
        mHMatrix = GLES20.glGetUniformLocation(mProgram, "vMatrix");
    }

    protected final void createProgramByAssetsFile(String vertex, String fragment) {
        createProgram(uRes(mRes, vertex), uRes(mRes, fragment));
    }

    /**
     * Buffer初始化
     */
    protected void initBuffer() {
        ByteBuffer a = ByteBuffer.allocateDirect(pos.length * 4);
        a.order(ByteOrder.nativeOrder());
        mVerBuffer = a.asFloatBuffer();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
    }

    public void resetCoord() {
        mVerBuffer.position(0);
    }

    protected void onUseProgram() {
        GLES20.glUseProgram(mProgram);
    }

    /**
     * 启用顶点坐标和纹理坐标进行绘制
     */
    protected void onDraw() {
        GLES20.glEnableVertexAttribArray(mHPosition);
        setVertex();
        GLES20.glUniform4fv(mHColor, 1, color, 0);
        drawShape();
        GLES20.glDisableVertexAttribArray(mHPosition);

    }

    protected void setVertex() {
        GLES20.glVertexAttribPointer(mHPosition, 2, GLES20.GL_FLOAT, false, 0, mVerBuffer);
    }

    protected void drawShape() {
        GLES20.glDrawArrays(getDrawMode(), 0, getDrawCount());
    }


    protected int getDrawMode() {
        return GLES20.GL_TRIANGLE_STRIP;
    }

    protected int getDrawCount() {
        return 4;
    }


    /**
     * 清除画布
     */
    protected void onClear() {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * 设置其他扩展数据
     */
    protected void onSetExpandData() {
        GLES20.glUniformMatrix4fv(mHMatrix, 1, false, matrix, 0);
    }


}

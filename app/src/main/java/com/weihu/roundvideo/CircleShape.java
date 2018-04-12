package com.weihu.roundvideo;

import android.content.res.Resources;
import android.opengl.GLES20;

public class CircleShape extends BaseShape {

    // 定义圆心坐标
    private float x;
    private float y;
    // 半径
    private float r;
    // 三角形分割的数量
    private int count = 50;
    // 每个顶点包含的数据个数 （ x 和 y ）
    private static final int POSITION_COMPONENT_COUNT = 2;

    public CircleShape(Resources mRes) {
        super(mRes);
        x = 0f;
        y = 0f;
        r = 1f;

        initVertexData();
        color = new float[]{0.0f, 1.0f, 1.0f, 1f};
    }

    @Override
    public void setMatrix(float[] matrix) {
        super.setMatrix(matrix);
    }

    private void initVertexData() {
        // 顶点的个数，我们分割count个三角形，有count+1个点，再加上圆心共有count+2个点
        final int nodeCount = count + 2;
        float circleCoords[] = new float[nodeCount * POSITION_COMPONENT_COUNT];
        // x y
        int offset = 0;
        circleCoords[offset++] = x;// 中心点
        circleCoords[offset++] = y;
        for (int i = 0; i < count + 1; i++) {
            float angleInRadians = ((float) i / (float) count)
                    * ((float) Math.PI * 2f);
            circleCoords[offset++] = x + r * (float) Math.sin(angleInRadians);
            circleCoords[offset++] = y + r * (float) Math.cos(angleInRadians);
        }

        pos = circleCoords;
    }

    @Override
    protected void onClear() {
        super.onClear();
    }

    @Override
    protected int getDrawMode() {
        return GLES20.GL_TRIANGLE_FAN;
    }

    @Override
    protected int getDrawCount() {
        return count + 2;
    }
}

package com.weihu.roundvideo;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.RectF;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class RoundShape extends BaseShape {
    private ShortBuffer mTriangleIndices;
    private GLRoundedGeometry.GeometryArrays vertexData;

    public RoundShape(Resources mRes) {
        super(mRes);

        initVertexData();
        color = new float[]{0.0f, 0.0f, 1.0f, 1f};
    }


    private void initVertexData() {

        RectF roundRadius = new RectF(0.1f, 0.1f, 0.1f, 0.1f);
        RectF roundedGeometry = new RectF(-1, 1, 1, -1);
        Point viewPortSize = new Point();
        viewPortSize.set(1, 1);

        GLRoundedGeometry glRoundedGeometry = new GLRoundedGeometry();
        vertexData = glRoundedGeometry.generateVertexData(roundRadius, roundedGeometry, viewPortSize);

        pos = vertexData.triangleVertices;

        mTriangleIndices = ByteBuffer.allocateDirect(
                vertexData.triangleIndices.length * 2)
                .order(ByteOrder.nativeOrder()).asShortBuffer();
        mTriangleIndices.put(vertexData.triangleIndices).position(0);
    }

    @Override
    protected void onDraw() {
//        super.onDraw();

        GLES20.glEnableVertexAttribArray(mHPosition);
        mVerBuffer.position(0);
        GLES20.glVertexAttribPointer(mHPosition, 3, GLES20.GL_FLOAT, false,
                20, mVerBuffer);
        GLES20.glUniform4fv(mHColor, 1, color, 0);
//
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, vertexData.triangleIndices.length,
                GL10.GL_UNSIGNED_SHORT, mTriangleIndices);
        GLES20.glDisableVertexAttribArray(mHPosition);
    }
}

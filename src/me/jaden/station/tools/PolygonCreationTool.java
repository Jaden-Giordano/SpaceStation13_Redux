package me.jaden.station.tools;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * Created by Jaden on 7/20/2015.
 */
public class PolygonCreationTool {


    public static final int RECTANGLE = 4;
    public static final int TRIANGLE = 3;

    public static final int VERTEX_SIZE = 2; // X, Y
    public static final int COLOR_SIZE = 4, TEXTURE_SIZE = 2; // Color: R, G, B, A Texture: U, V

    /**
     * Creates the vertices and color of each vertex of a Rectangle
     *
     * @param w
     * @param h
     * @return -Returns the VertexBuffer and the ColorBuffer. First(0) index of array is VertexData and the Second(1) is the ColorData.
     */
    public static FloatBuffer createRectangle(float w, float h) {
        FloatBuffer vertex_data = BufferUtils.createFloatBuffer(RECTANGLE * VERTEX_SIZE);
        vertex_data.put(new float[] { 0, 0 });
        vertex_data.put(new float[] { w, 0 });
        vertex_data.put(new float[] { w, h });
        vertex_data.put(new float[] { 0, h });

        vertex_data.flip();

        return vertex_data;
    }

    /**
     * Creates the vertices and color of each vertex of a Triangle
     *
     * @param w
     * @param h
     * @return -Returns the VertexBuffer and the ColorBuffer. First(0) index of array is VertexData and the Second(1) is the ColorData.
     */
    public static FloatBuffer createRightTriangle(float w, float h) {
        FloatBuffer vertex = BufferUtils.createFloatBuffer(TRIANGLE * VERTEX_SIZE);
        vertex.put(new float[] { 0, 0 });
        vertex.put(new float[] { 0, h });
        vertex.put(new float[] { w, 0 });

        vertex.flip();

        return vertex;
    }

    public static FloatBuffer createRegularNGon(int n) {
        FloatBuffer vertex = BufferUtils.createFloatBuffer(n * VERTEX_SIZE);
        for (int i = 0; i < n; i++) {
            vertex.put(new float[] { (float) (Math.cos((2 * Math.PI * i) / n) + 1), (float) (Math.sin((2 * Math.PI * i) / n) + 1) });
        }

        vertex.flip();

        return vertex;
    }

    // TODO: Fix
    public static FloatBuffer createIrregularNGon(int n, float[] r) {
        if (r.length > n) return null;

        FloatBuffer vertex = BufferUtils.createFloatBuffer(n * VERTEX_SIZE);
        for (int i = 0; i < n; i++) {
            float _r = (r[i % r.length] + r[(i + 1) % r.length]) / 2;
            System.out.println("_r, " + _r);
            vertex.put(new float[] { (float) (_r * Math.cos((2 * Math.PI * i) / n)), (float) (_r * Math.sin((2 * Math.PI * i) / n)) });
        }

        vertex.flip();

        return vertex;
    }

    /**
     * Creates color data and outputs it in a FloatBuffer.
     *
     * @param r
     *            -Red
     * @param g
     *            -Green
     * @param b
     *            -Blue
     * @param a
     *            -Alpha
     * @param vs
     *            -Vertices
     * @return Returns FloatBuffer of color data.
     */
    public static FloatBuffer createColorData(float r, float g, float b, float a, int vs) {
        FloatBuffer color = BufferUtils.createFloatBuffer(vs * COLOR_SIZE);

        for (int i = 0; i < vs; i++) { // Sets each vertex to color specified
            color.put(new float[] { r, g, b, a });
        }

        color.flip();

        return color;
    }

    public static FloatBuffer createTextureCoords(FloatBuffer vertex_data) {
        int vertices = vertex_data.limit() / VERTEX_SIZE;
        FloatBuffer texture_data = BufferUtils.createFloatBuffer(vertices * TEXTURE_SIZE);

        texture_data.put(new float[] { 0, 0 }); //Top right
        texture_data.put(new float[] { 1, 0 }); //top left
        texture_data.put(new float[] { 1, 1 }); //bottom left
        texture_data.put(new float[] { 0, 1 }); //Bottom right

		/*
		 * for (int i = 0; i < vertices*VERTEX_SIZE; i += 2) { texture_data.put(new float[] {vertex_data.get(i), vertex_data.get(i+1)}); }
		 */

        texture_data.flip();

        return texture_data;
    }


}

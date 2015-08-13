package me.jaden.station.gui;

import info.rockscode.util.Texture;
import info.rockscode.util.Vector2f;
import me.jaden.station.tools.PolygonCreationTool;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

/**
 * Created by Jaden on 8/4/2015.
 */
public class GuiCloseButton extends GuiElement {

    protected int v_handle, c_handle;
    protected int vertices;
    protected FloatBuffer v_data, c_data;

    public GuiCloseButton(Vector2f pos) {
        super();

        this.pos = new Vector2f(pos.x, pos.y);
        this.width = 20;
        this.height = 20;
        v_data = PolygonCreationTool.createRectangle(this.width, this.height);
        vertices = v_data.capacity() / PolygonCreationTool.VERTEX_SIZE;
        c_data = PolygonCreationTool.createColorData(1f, 0f, 0f, 1, PolygonCreationTool.RECTANGLE);
        generateBuffers();
    }

    public void update(double delta) {
    }

    public void render() {
        glPushMatrix();
        {
            glTranslatef(this.pos.x, this.pos.y, 0);

            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ARRAY_BUFFER, v_handle);
            glVertexPointer(PolygonCreationTool.VERTEX_SIZE, GL_FLOAT, 0, 0l);

            glEnableClientState(GL_COLOR_ARRAY);
            glBindBuffer(GL_ARRAY_BUFFER, c_handle);
            glColorPointer(PolygonCreationTool.COLOR_SIZE, GL_FLOAT, 0, 0l);

            glDrawArrays(GL_POLYGON, 0, vertices);

            glDisableClientState(GL_VERTEX_ARRAY);
            glDisableClientState(GL_COLOR_ARRAY);

            Texture.unbind();
        }
        glPopMatrix();
    }

    public void cleanUp() {
        glDeleteBuffers(v_handle);
        glDeleteBuffers(c_handle);
    }

    public void reconstruct() {
        cleanUp();
        generateBuffers();
    }

    private void generateBuffers() {
        v_handle = glGenBuffers();
        vertexBufferData(v_handle, v_data);

        c_handle = glGenBuffers();
        vertexBufferData(c_handle, c_data);
    }

    private void vertexBufferData(int id, FloatBuffer buffer) {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }


}

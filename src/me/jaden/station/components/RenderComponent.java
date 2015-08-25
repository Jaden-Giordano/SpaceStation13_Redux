package me.jaden.station.components;

import info.rockscode.util.Texture;
import me.jaden.station.Camera;
import me.jaden.station.GameObject;
import me.jaden.station.Station;
import me.jaden.station.tools.PolygonCreationTool;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * Created by Jaden on 7/20/2015.
 */
public class RenderComponent {

    protected Texture tex;

    protected int v_handle, t_handle;
    protected int vertices;
    protected FloatBuffer v_data, t_data;

    protected GameObject parentGameObject;

    public RenderComponent(GameObject parentGameObject) {
        this.parentGameObject = parentGameObject;

        tex = new Texture(Texture.genNotFoundBufIMG(), false);

        v_data = PolygonCreationTool.createRectangle(tex.getImage().getWidth()/4, tex.getImage().getHeight()/4);
        vertices = v_data.capacity() / PolygonCreationTool.VERTEX_SIZE;
        t_data = PolygonCreationTool.createTextureCoords(v_data);

        generateBuffers();
    }

    public RenderComponent(GameObject parentGameObject, FloatBuffer v_data) {
        this.parentGameObject = parentGameObject;

        tex = new Texture(Texture.genNotFoundBufIMG(), false);

        this.v_data = v_data;
        vertices = v_data.capacity() / PolygonCreationTool.VERTEX_SIZE;
        t_data = PolygonCreationTool.createTextureCoords(v_data);

        generateBuffers();
    }

    public void update(double delta) {
    }

    public void render() {
        glPushMatrix();
        {
            glColor3f(1.0f, 1.0f, 1.0f);

            this.tex.bind();

            Camera c = Station.instance.getGame().getCamera();
            glTranslatef(this.parentGameObject.getPosition().x - c.getPosition().x, this.parentGameObject.getPosition().y - c.getPosition().y, -this.parentGameObject.getPosition().z/10);

            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ARRAY_BUFFER, v_handle);
            glVertexPointer(PolygonCreationTool.VERTEX_SIZE, GL_FLOAT, 0, 0l);

            glEnableClientState(GL_TEXTURE_COORD_ARRAY);
            glBindBuffer(GL_ARRAY_BUFFER, t_handle);
            glTexCoordPointer(PolygonCreationTool.TEXTURE_SIZE, GL_FLOAT, 0, 0l);

            glDrawArrays(GL_POLYGON, 0, vertices);

            glDisableClientState(GL_VERTEX_ARRAY);
            glDisableClientState(GL_TEXTURE_COORD_ARRAY);

            Texture.unbind();
        }
        glPopMatrix();
    }

    public void cleanUp() {
        glDeleteBuffers(v_handle);
        glDeleteBuffers(t_handle);
    }

    public void reconstruct() {
        cleanUp();
        generateBuffers();
    }

    public void setTexture(Texture tex) {
        this.tex = tex;
    }

    public Texture getTexture() {
        return this.tex;
    }

    private void generateBuffers() {
        v_handle = glGenBuffers();
        vertexBufferData(v_handle, v_data);

        t_handle = glGenBuffers();
        vertexBufferData(t_handle, t_data);
    }

    private void vertexBufferData(int id, FloatBuffer buffer) {
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

}

package me.jaden.station.objects.tiles;

import info.rockscode.util.Texture;
import info.rockscode.util.Vector2f;
import me.jaden.station.Camera;
import me.jaden.station.Station;
import me.jaden.station.tools.Constants;
import me.jaden.station.tools.PolygonCreationTool;
import me.jaden.station.tools.TextureLoader;

import java.io.File;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * Created by Jaden on 7/28/2015.
 */
public class Space {

    private Vector2f pos;

    Texture tex;

    private int v_handle, t_handle;
    private int vertices;
    private FloatBuffer v_data, t_data;

    public Space() {
        this.tex = TextureLoader.loadTexture(Constants.assetsPath + "tiles" + File.separator + "space.png");

        v_data = PolygonCreationTool.createRectangle(32, 32);
        vertices = v_data.capacity() / PolygonCreationTool.VERTEX_SIZE;
        t_data = PolygonCreationTool.createTextureCoords(v_data);

        generateBuffers();
    }

    public void render() {
        Vector2f cam = Station.instance.getGame().getCamera().getPosition();
        if ((this.getPosition().x < cam.x+Constants.width && this.getPosition().x+32 > cam.x) && (this.getPosition().y < cam.y+Constants.height && this.getPosition().y+32 > cam.y)) {
            glPushMatrix();
            {
                glColor3f(1.0f, 1.0f, 1.0f);

                this.tex.bind();

                Camera c = Station.instance.getGame().getCamera();
                glTranslatef(this.getPosition().x - c.getPosition().x, this.getPosition().y - c.getPosition().y, 0);

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

    public Vector2f getPosition() {
        return this.pos;
    }

    public void setPosition(Vector2f v) {
        this.pos = v;
    }

}

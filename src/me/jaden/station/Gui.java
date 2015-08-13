package me.jaden.station;

import info.rockscode.util.Texture;
import info.rockscode.util.Vector2f;
import me.jaden.station.events.KeyEvent;
import me.jaden.station.events.MouseEvent;
import me.jaden.station.gui.GuiCloseButton;
import me.jaden.station.gui.GuiElement;
import me.jaden.station.tools.PolygonCreationTool;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

/**
 * Created by Jaden on 8/3/2015.
 */
public class Gui {

    protected Vector2f pos;

    protected int v_handle, c_handle;
    protected int vertices;
    protected FloatBuffer v_data, c_data;

    protected List<GuiElement> elements;

    protected boolean close;

    public Gui(Vector2f pos, float w, float h) {
        this.elements = new ArrayList<GuiElement>();

        this.pos = pos;

        this.close = false;

        v_data = PolygonCreationTool.createRectangle(w, h);
        vertices = v_data.capacity() / PolygonCreationTool.VERTEX_SIZE;
        c_data = PolygonCreationTool.createColorData(.3f, .3f, .3f, 1, PolygonCreationTool.RECTANGLE);
        generateBuffers();

        GuiCloseButton b = (GuiCloseButton) this.add(new GuiCloseButton(this.pos));
        b.setClickFunc(new Runnable() {
            @Override
            public void run() {
                close = true;
            }
        });
    }

    public GuiElement add(GuiElement e) {
        this.elements.add(e);
        return e;
    }

    public void update(double delta) {
        for (GuiElement i : this.elements) {
            i.update(delta);
        }
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

        for (GuiElement i : this.elements) {
            i.render();
        }
    }

    public void cleanUp() {
        for (GuiElement i : this.elements) {
            i.cleanUp();
        }
        glDeleteBuffers(v_handle);
        glDeleteBuffers(c_handle);
    }

    public void reconstruct() {
        for (GuiElement i : this.elements) {
            i.reconstruct();
        }
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

    public void keyInput(KeyEvent e) {

    }

    public void mouseInput(MouseEvent e) {
        for (GuiElement i : this.elements) {
            Station.instance.getLogger().log(e.getX()+","+e.getY()+" : "+i.getPos().x+","+i.getPos().y+" "+i.getWidth()+","+i.getHeight());
            if ((e.getX() >= i.getPos().x && e.getX() <= i.getPos().x+i.getWidth())
                    && (e.getY() >= i.getPos().y && e.getY() <= i.getPos().y+i.getHeight())) {
                i.onClick();
            }
        }
    }

    public boolean isClosed() {
        return this.close;
    }

}

package me.jaden.station.components;

import info.rockscode.util.Texture;
import me.jaden.station.*;
import me.jaden.station.tools.PolygonCreationTool;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;

/**
 * Created by Jaden on 8/1/2015.
 */
public class AnimationComponent extends RenderComponent {

    private Animation anim;

    public AnimationComponent(GameObject o) {
        super(o);
    }

    public void setAnimation(Animation anim) {
        this.anim = anim;
    }

    public Animation getAnimation() {
        return this.anim;
    }

    public void update(double delta) {
        if (this.anim != null) {
            anim.update(delta);
        }
    }

    public void render() {
        if (this.anim != null) {
            glPushMatrix();
            {
                glColor3f(1.0f, 1.0f, 1.0f);

                this.anim.bind();

                Camera c = Station.instance.getGame().getCamera();
                glTranslatef(this.parentGameObject.getPosition().x - c.getPosition().x, this.parentGameObject.getPosition().y - c.getPosition().y, 0);

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

}

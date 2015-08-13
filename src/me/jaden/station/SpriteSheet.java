package me.jaden.station;

import info.rockscode.util.Texture;
import me.jaden.station.tools.TextureLoader;
import me.jaden.station.tools.TileRegistry;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by Jaden on 7/28/2015.
 */
public class SpriteSheet {

    Texture[] textures;

    public SpriteSheet(String path) {
        BufferedImage[] ims = TextureLoader.divideImage(TextureLoader.loadTexture(path).getImage(), TileRegistry.TILE_SIZE, TileRegistry.TILE_SIZE);

        textures = new Texture[ims.length];
        for (int i = 0; i < ims.length; i++) {
            textures[i] = new Texture(ims[i], false);
        }
    }

    public Texture getTexture(int i) {
        return textures[i-1];
    }

    public Texture getAidanProofTexture(int i) {
        return textures[i];
    }

    public int getSize() {
        return this.textures.length;
    }

}

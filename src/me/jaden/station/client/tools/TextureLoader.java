package me.jaden.station.client.tools;

import info.rockscode.util.Texture;
import me.jaden.station.client.SpriteSheet;
import me.jaden.station.client.Station;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jaden on 7/20/2015.
 */
public class TextureLoader {

    private static Map<String, Texture> textures;

    private static SpriteSheet base;

    public static void init() {
        textures = new HashMap<String, Texture>();

        base = new SpriteSheet(Constants.assetsPath+"sheets"+File.separator+"base.png");
    }

    public static Texture loadTexture(String path) {
        Texture t;
        if ((t = textures.get(path)) != null) {
            return t;
        } else {
            try {
                t = new Texture(new File(path), false);
                textures.put(path, t);
                return t;
            } catch (Exception e) {
                Station.instance.getLogger().log(e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }

    public static BufferedImage[] divideImage(BufferedImage img, int divideWidth, int divideHeight) {
        try {
            int a=img.getWidth()/divideWidth,b=img.getHeight()/divideHeight;
            BufferedImage[] images=new BufferedImage[a*b];
            int i=0,thex=0;
            for(int they=0;i<images.length;) {
                if(i<a*b+1){
                    images[i]=img.getSubimage(thex,they,divideWidth,divideHeight);
                    i++;thex+=divideWidth;
                    if(thex==img.getWidth()) {
                        they+=divideHeight;
                        thex=0;
                    }}
            }
            return images;
        }catch(Exception e) {e.printStackTrace();return null;}
    }

    public static Texture getTileFromBaseSheet(int id) {
        return base.getTexture(id);
    }

    public static Texture getTileFromSheet(int id, String sheet) {
        if (sheet.equalsIgnoreCase("turf")) {
            return base.getTexture(id);
        }
        return base.getTexture(id);
    }

}

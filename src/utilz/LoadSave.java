package utilz;

import game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoadSave {

    public static final String PLAYER_ATLAS = "resources/player_sprites.png";
    public static final String LEVEL_ATLAS = "resources/outside_sprites.png";
//    public static final String LEVEL_ONE_DATA = "resources/level_one_data.png";
    public static final String LEVEL_ONE_DATA = "resources/level_one_data_long.png";
    public static final String MENU_BUTTONS = "resources/button_atlas.png";
    public static final String MENU_BACKGROUND = "resources/menu_background.png";
    public static final String MENU_BACKGROUND_IMAGE = "resources/background_menu.png";
    public static final String PAUSE_BACKGROUND = "resources/pause_menu.png";
    public static final String SOUND_BUTTONS = "resources/sound_buttons.png";
    public static final String URM_BUTTONS = "resources/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "resources/volume_buttons.png";

    public static BufferedImage GetSpriteAtlas(String filename) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.out.println("Error loading player image at: ");
            e.printStackTrace();
        }
        return image;
    }

    public static int[][] GetLevelData() {
        BufferedImage image = GetSpriteAtlas(LEVEL_ONE_DATA);
        int[][] levelData = new int[image.getHeight()][image.getWidth()];

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getRed();
                if(value >= 48) value = 0;
                levelData[j][i] = value;
            }
        }

        return levelData;
    }
}

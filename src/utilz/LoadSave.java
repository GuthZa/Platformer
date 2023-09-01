package utilz;

import entities.Crabby;
import game.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import static utilz.Constants.Enemy.CRABBY;

public class LoadSave {
    public static final String LEVEL_ATLAS = "resources/outside_sprites.png";
    //ENTITY SPECIFICS
    public static final String PLAYER_ATLAS = "resources/player_sprites.png";
    public static final String CRABBY_SPRITE = "resources/crabby_sprite.png";
    public static final String STATUS_BAR = "resources/health_power_bar.png";

    //MENU
    public static final String MENU_BUTTONS = "resources/button_atlas.png";
    public static final String MENU_BACKGROUND = "resources/menu_background.png";
    public static final String MENU_BACKGROUND_IMAGE = "resources/background_menu.png";

    //BUTTONS (PAUSE, SOUND, REPLAY, MUTE)
    public static final String PAUSE_BACKGROUND = "resources/pause_menu.png";
    public static final String SOUND_BUTTONS = "resources/sound_buttons.png";
    public static final String URM_BUTTONS = "resources/urm_buttons.png";
    public static final String VOLUME_BUTTONS = "resources/volume_buttons.png";
    public static final String PLAYING_BACKGROUND_IMAGE = "resources/playing_bg_img.png";

    //PLAYING BACKGROUND
    public static final String PLAYING_SMALL_CLOUDS = "resources/small_clouds.png";
    public static final String PLAYING_BIG_CLOUDS = "resources/big_clouds.png";
    public static final String LEVEL_COMPLETED = "resources/level_completed.png";

    //OBJECTS
    public static final String POTION_ATLAS = "resources/potions_sprites.png";
    public static final String CONTAINER_ATLAS = "resources/objects_sprites.png";

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

    public static BufferedImage[] GetAllLevels() {

        String pathName = "resources/levelData";

        File folder = new File(pathName);
        File[] listOfFiles = folder.listFiles();
//
//        for (File listOfFile : listOfFiles) {
//            if (listOfFile.isFile()) {
//                System.out.println("File " + listOfFile.getName());
//            } else if (listOfFile.isDirectory()) {
//                System.out.println("Directory " + listOfFile.getName());
//            }
//        }

        File[] files = folder.listFiles();
        File[] filesSorted = new File[listOfFiles.length];

        for (int i = 0; i < filesSorted.length; i++)
            for (int j = 0; j < listOfFiles.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png"))
                    filesSorted[i] = files[j];

            }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];

        for (int i = 0; i < imgs.length; i++)
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }

        return imgs;
    }
}

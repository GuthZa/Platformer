package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton {
    private BufferedImage[][] soundImages;

    public SoundButton(int x, int y, int width, int height) {
        super(x, y, width, height);
        loadSoundImages();
    }

    private void loadSoundImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        soundImages = new BufferedImage[2][3];
        for (int j = 0; j < soundImages.length; j++) {
            for (int i = 0; i < soundImages[j].length; i++) {
                soundImages[j][i] = temp.getSubimage(i * SOUND_BUTTON_DEFAULT_SIZE, j * SOUND_BUTTON_DEFAULT_SIZE,
                        SOUND_BUTTON_DEFAULT_SIZE, SOUND_BUTTON_DEFAULT_SIZE);
            }
        }
    }

    public void draw(Graphics g) {
        g.drawImage(soundImages[0][0], x, y, width, height, null);
    }

    public void update() {

    }
}

package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;

public class SoundButton extends PauseButton {
    private BufferedImage[][] soundImages;
    private boolean mouseOver, mousePressed;
    private boolean muted;
    private int rowIndex, columnIndex;

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
        g.drawImage(soundImages[rowIndex][columnIndex], x, y, width, height, null);
    }

    public void update() {
        if(muted) rowIndex = 1;
        else rowIndex = 0;

        columnIndex = 0;
        if(mouseOver) columnIndex = 1;
        if(mousePressed) columnIndex = 2;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public boolean isMuted() {
        return !muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}

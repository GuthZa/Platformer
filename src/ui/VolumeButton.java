package ui;

import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton {

    private BufferedImage[] image;
    private BufferedImage slider;
    private boolean mouseOver, mousePressed;
    private int index, buttonX, minX, maxX;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_BUTTON_WIDTH, height);
        bounds.x -= VOLUME_BUTTON_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        this.minX = x + VOLUME_BUTTON_WIDTH / 2;
        this.maxX = x + width - VOLUME_BUTTON_WIDTH / 2;
        loadImages();
    }

    private void loadImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
        image = new BufferedImage[3];

        for (int i = 0; i < image.length; i++) {
            image[i] = temp.getSubimage(i * VOLUME_BUTTON_DEFAULT_WIDTH, 0, VOLUME_BUTTON_DEFAULT_WIDTH, VOLUME_BUTTON_DEFAULT_HEIGHT);
        }

        slider = temp.getSubimage(3 * VOLUME_BUTTON_DEFAULT_WIDTH, 0, VOLUME_SLIDER_DEFAULT_WIDTH, VOLUME_BUTTON_DEFAULT_HEIGHT);
    }

    public void draw(Graphics g) {
        g.drawImage(slider, x, y, width, height, null);
        g.drawImage(image[index], buttonX - VOLUME_BUTTON_WIDTH / 2, y, VOLUME_BUTTON_WIDTH, height, null);
    }

    public void update() {
        index = 0;
        if(mouseOver) index = 1;
        if(mousePressed) index = 2;
    }

    public void changeX(int x) {
        if (x > maxX) buttonX = maxX;
        else if (x < minX) buttonX = minX;
        else buttonX = x;

        bounds.x = buttonX - VOLUME_BUTTON_WIDTH / 2;
    }

    public void resetBooleans() {
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
}

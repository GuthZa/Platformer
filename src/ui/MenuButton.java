package ui;

import gamestates.GameState;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.Buttons.*;

public class MenuButton {

    private GameState state;
    private BufferedImage[] images;

    private int xPosition, yPosition, rowIndex, index;
    private int xOffSetCenter = BUTTON_WIDTH / 2;
    private Rectangle bounds;
    private boolean mouseOver, mousePressed;
    public MenuButton(int xPosition, int yPosition, GameState state, int rowIndex) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.state = state;
        this.rowIndex = rowIndex;
        loadImage();
        initBounds();
    }

    private void loadImage() {
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * BUTTON_WIDTH_DEFAULT,
                    rowIndex * BUTTON_HEIGHT_DEFAULT,
                    BUTTON_WIDTH_DEFAULT, BUTTON_HEIGHT_DEFAULT);
        }
    }

    private void initBounds() {
        bounds = new Rectangle(xPosition - xOffSetCenter, yPosition, BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    public void draw(Graphics g) {
        g.drawImage(images[index], xPosition - xOffSetCenter, yPosition,
                BUTTON_WIDTH, BUTTON_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if(mouseOver)
            index = 1;
        if(mousePressed)
            index = 2;
    }


    public void applyGameState() {
        GameState.state = state;
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

    public Rectangle getBounds() {
        return bounds;
    }
}

package ui;

import gamestates.GameState;
import utilz.LoadSave;

import java.awt.image.BufferedImage;

public class MenuButton {

    private int xPosition, yPosition, rowIndex;
    private GameState state;
    private BufferedImage[] images;
    public MenuButton(int xPosition, int yPosition, GameState state, int rowIndex) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.state = state;
        this.rowIndex = rowIndex;
        loadImage();
    }

    private void loadImage() {
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < images.length; i++) {
            temp.getSubimage(xPosition, yPosition,)
        }
    }
}

package ui;

import game.Game;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.URMButtons.*;

public class LevelCompletedOverlay {

    private Playing playing;
    private UrmButton menu, next;
    private BufferedImage image;
    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;

    public LevelCompletedOverlay(Playing playing) {
        this.playing = playing;
        initImage();
        initButtons();
    }

    private void initImage() {
        image = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETED);
        backgroundWidth = (int) (image.getWidth() * Game.SCALE);
        backgroundHeight = (int) (image.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (75 * Game.SCALE);
    }

    private void initButtons() {
        int menuX = (int) (330 * Game.SCALE);
        int nextX = (int) (445 * Game.SCALE);
        int y = (int) (195 * Game.SCALE);
        next = new UrmButton(nextX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
        menu = new UrmButton(menuX, y, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
    }

    public void draw(Graphics g) {
        g.drawImage(image, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);
        next.draw(g);
        menu.draw(g);
    }

    public void update() {
        next.update();
        menu.update();
    }

    private boolean isIn(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }


    public void mouseMoved(MouseEvent e) {
        next.setMouseOver(false);
        menu.setMouseOver(false);

        if(isIn(e, menu))
            menu.setMouseOver(true);
        else if(isIn(e, next))
            next.setMouseOver(true);
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, menu) && menu.isMousePressed())
            System.out.println("menu");
        else if(isIn(e, next) && next.isMousePressed())
            System.out.println("next");

        menu.resetBooleans();
        next.resetBooleans();
    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, menu))
            menu.setMousePressed(true);
        else if(isIn(e, next))
            next.setMousePressed(true);
    }
}

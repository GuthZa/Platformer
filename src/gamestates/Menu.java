package gamestates;

import game.Game;
import ui.MenuButton;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static game.Game.*;

public class Menu extends State implements StateMethods {

    private final MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundMenuImage;
    private BufferedImage backgroundImage;
    private int menuX, menuY, menuWidth, menuHeight;

    public Menu(Game game) {
        super(game);
        loadBackground();
        loadButtons();
    }

    @Override
    public void update() {
        for (MenuButton menuButton : buttons) {
            menuButton.update();
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);
        g.drawImage(backgroundMenuImage, menuX, menuY, menuWidth, menuHeight, null);

        for (MenuButton menuButton : buttons)
            menuButton.draw(g);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(GAME_WIDTH / 2, (int) (150 * SCALE), GameState.PLAYING, 0);
        buttons[1] = new MenuButton(GAME_WIDTH / 2, (int) (220 * SCALE), GameState.OPTIONS, 1);
        buttons[2] = new MenuButton(GAME_WIDTH / 2, (int) (290 * SCALE), GameState.QUIT, 2);
    }

    private void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMAGE);
        backgroundMenuImage = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth = (int) (backgroundMenuImage.getWidth() * SCALE);
        menuHeight = (int) (backgroundMenuImage.getHeight() * SCALE);
        menuX = GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * SCALE);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            mb.setMouseOver(false);

        for (MenuButton menuButton : buttons) {
            if(isIn(e, menuButton)) {
                menuButton.setMouseOver(true);
                break;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (isIn(e, menuButton)) {
                menuButton.setMousePressed(true);
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton menuButton : buttons) {
            if (isIn(e, menuButton) && menuButton.isMousePressed()) {
                menuButton.applyGameState();
                break;
            }
        }
        resetButtons();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER) GameState.state = GameState.PLAYING;
    }

    private void resetButtons() {
        for (MenuButton mb: buttons) {
            mb.resetBooleans();
        }
    }
}

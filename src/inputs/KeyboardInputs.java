package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import game.GamePanel;

import static utilz.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {

    private final GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gamePanel.setDirection(UP);
            case KeyEvent.VK_D -> gamePanel.setDirection(RIGHT);
            case KeyEvent.VK_A -> gamePanel.setDirection(LEFT);
            case KeyEvent.VK_S -> gamePanel.setDirection(DOWN);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D -> gamePanel.setMoving(false);
        }
    }
}

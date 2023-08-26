package gamestates;

import entities.Player;
import game.Game;
import levels.LevelManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static game.Game.SCALE;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
    }

    @Override
    public void update() {
        levelManager.update();
        player.update();

    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g);
        player.render(g);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.setJump(true);
            case KeyEvent.VK_BACK_SPACE -> GameState.state = GameState.MENU;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
            case KeyEvent.VK_SPACE -> player.setJump(false);
        }
    }

    public Player getPlayer() { return player; }
}

package gamestates;

import entities.Player;
import game.Game;
import levels.LevelManager;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static game.Game.*;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean paused;

    private final int leftBorder = (int) (0.2 * GAME_WIDTH);
    private final int rightBorder = (int) (0.8 * GAME_WIDTH);
    private int xLevelOffSet = 0;
    private final int levelTilesWide = LoadSave.GetLevelData()[0].length;
    private final int maxTilesOffSet = levelTilesWide - TILES_IN_WIDTH;
    private final int maxLevelOffSetX = maxTilesOffSet * TILES_SIZE;

    public Playing(Game game) {
        super(game);
        initClasses();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * SCALE), (int) (40 * SCALE));
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
    }

    @Override
    public void update() {
        if(!paused) {
            levelManager.update();
            player.update();
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitBox().x;
        int diff = playerX - xLevelOffSet;

        if (diff > rightBorder) xLevelOffSet += diff - rightBorder;
        else if (diff < leftBorder) xLevelOffSet += diff - leftBorder;


        if (xLevelOffSet > maxLevelOffSetX)
            xLevelOffSet = maxLevelOffSetX;
        else if (xLevelOffSet < 0)
            xLevelOffSet = 0;
    }

    @Override
    public void draw(Graphics g) {
        levelManager.draw(g, xLevelOffSet);
        player.render(g, xLevelOffSet);

        if(paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,GAME_WIDTH, GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(paused)
            pauseOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(paused)
            pauseOverlay.mouseReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.setJump(true);
            case KeyEvent.VK_BACK_SPACE -> GameState.state = GameState.MENU;
            case KeyEvent.VK_ESCAPE -> paused = !paused;
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

    public void unpauseGame() { this.paused = false; }

    public Player getPlayer() { return player; }
}

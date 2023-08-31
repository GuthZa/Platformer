package gamestates;

import entities.EnemyManager;
import entities.Player;
import game.Game;
import levels.LevelManager;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import static game.Game.*;
import static utilz.Constants.Environment.*;

public class Playing extends State implements StateMethods {
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private boolean paused;
    private boolean gameOver;

    //Moving the screen based on player position and borders
    private final int leftBorder = (int) (0.2 * GAME_WIDTH);
    private final int rightBorder = (int) (0.8 * GAME_WIDTH);
    private int xLevelOffSet = 0;
    private final int levelTilesWide = LoadSave.GetLevelData()[0].length;
    private final int maxTilesOffSet = levelTilesWide - TILES_IN_WIDTH;
    private final int maxLevelOffSetX = maxTilesOffSet * TILES_SIZE;

    //Environment
    private BufferedImage backgroundImage;
    private BufferedImage bigClouds;
    private BufferedImage smallClouds;
    private int[] smallCloudsPos;
    private final Random random = new Random();

    public Playing(Game game) {
        super(game);
        initClasses();
        initEnvironment();
    }

    private void initClasses() {
        levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
        player = new Player(200, 200, (int) (64 * SCALE), (int) (40 * SCALE), this);
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
    }

    private void initEnvironment() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMAGE);
        bigClouds = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BIG_CLOUDS);
        smallClouds = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_SMALL_CLOUDS);
        smallCloudsPos = new int[8];
        for (int i = 0; i < smallCloudsPos.length; i++) {
            smallCloudsPos[i] = (int) (90 * SCALE) + random.nextInt((int) (120 * SCALE));
        }
    }

    @Override
    public void update() {
        if(!paused && !gameOver) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
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
        g.drawImage(backgroundImage, 0, 0, GAME_WIDTH, GAME_HEIGHT, null);

        drawClouds(g);

        levelManager.draw(g, xLevelOffSet);
        player.render(g, xLevelOffSet);
        enemyManager.draw(g, xLevelOffSet);

        if(paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0,GAME_WIDTH, GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if(gameOver) {
            gameOverOverlay.draw(g);
        }
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++) {
            g.drawImage(bigClouds, i * BIG_CLOUD_WIDTH - (int) (xLevelOffSet * 0.3), (int) (204 * SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }

        for (int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(smallClouds, i * 4 * SMALL_CLOUD_WIDTH - (int) (xLevelOffSet * 0.7), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }
    }

    //checks
    public void checkEnemyHit(Rectangle2D.Float attackHitBox) {
        enemyManager.checkEnemyHit(attackHitBox);
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    //Key presses
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
        if(!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(paused)
            pauseOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused)
            pauseOverlay.mouseReleased(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
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
        if (!gameOver)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A -> player.setLeft(false);
                case KeyEvent.VK_D -> player.setRight(false);
                case KeyEvent.VK_SPACE -> player.setJump(false);
            }
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
    }

    public void unpauseGame() { this.paused = false; }

    public Player getPlayer() { return player; }
}

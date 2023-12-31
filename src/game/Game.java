package game;

import gamestates.GameState;
import gamestates.Menu;
import gamestates.Playing;

import java.awt.*;

public class Game implements Runnable {

    private final GameWindow gameWindow;
    private final GamePanel gamePanel;
    private Thread gameThread;
    private Playing playing;
    private Menu menu;


    private final int FPS = 120;
    private final int UPS = 200; //updates per second

    public final static int TILES_DEFAULT_SIZE = 32;
    public final static float SCALE = 2.0f;
    public final static int TILES_IN_WIDTH = 26;
    public final static int TILES_IN_HEIGHT = 14;
    public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public final static int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public final static int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public Game() {

        initClasses();

        gamePanel = new GamePanel(this);
        gameWindow = new GameWindow(gamePanel);
        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGameLoop();
    }

    private void initClasses() {
        playing = new Playing(this);
        menu = new Menu(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void updateGame() {
        switch (GameState.state) {
            case MENU -> menu.update();
            case PLAYING -> playing.update();
            case OPTIONS, QUIT -> System.exit(0);
            default -> {
                System.out.println("Error on states");
                System.exit(0);
            }
        }
    }

    public void render(Graphics g) {
        switch (GameState.state) {
            case MENU -> menu.draw(g);
            case PLAYING -> playing.draw(g);
            default -> System.out.println("Error on states");
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS;
        double timePerUpdate = 1000000000.0 / UPS;//frequency of updates

        long lastCheck = System.currentTimeMillis();
        long previousTime = System.nanoTime();

        int frames = 0;
        int updates = 0;

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            long currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(deltaU >= 1) {
                updateGame();
                updates++;
                deltaU--;
            }

            if(deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if(System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if(GameState.state == GameState.PLAYING)
            playing.getPlayer().resetDirBooleans();
    }

    public Playing getPlaying() { return playing; }

    public Menu getMenu() { return menu; }
}

package game;

public class Game implements Runnable {

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private final int FPS = 120;

    public Game() {
        gamePanel = new GamePanel();
        gameWindow = new GameWindow(gamePanel);
        gamePanel.requestFocus();
    }

    @Override
    public void run() {

        double timePerFrame = 1000000000 / FPS;

        while (true) {

        }
    }
}

package game;

import game.GamePanel;

import javax.swing.*;

public class GameWindow extends JFrame{
    public GameWindow(GamePanel gamePanel) {
        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(gamePanel);
        setLocationRelativeTo(null);


        setVisible(true);
    }
}

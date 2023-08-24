package game;

import game.GamePanel;

import javax.swing.*;

public class GameWindow extends JFrame{
    public GameWindow(GamePanel gamePanel) {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(gamePanel);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();

        setVisible(true);
    }
}

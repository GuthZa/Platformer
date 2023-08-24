package game;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel {

    private float xDelta = 100, yDelta = 100;
    private float xDir = 0.4f, yDir = 0.4f;
    private MouseInputs mouseInputs;
    private Color color = new Color(150, 20, 90);
    private Random random;
    public GamePanel() {
        random = new Random();
        mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeXDelta(int value) {
        this.xDelta+=value;
    }

    public void changeYDelta(int value) {
        this.yDelta+=value;
    }

    public void setRectPosition(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
    }
    public void paint(Graphics g) {
        super.paintComponent(g);

        updateRectangle();
        g.setColor(color);
        g.fillRect((int) xDelta, (int) yDelta, 20, 20);


    }

    private void updateRectangle() {
        xDelta+=xDir;
        if(xDelta > 400 || xDelta < 0) { //width
            xDir*=-1;
            color = getRandomColor();
        }
        yDelta+=yDir;
        if(yDelta > 400 || yDelta < 0) { //height
            yDir*=-1;
        }
    }

    private Color getRandomColor() {
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return new Color(r,b,g);
    }
}

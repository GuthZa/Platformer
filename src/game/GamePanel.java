package game;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import utilz.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static utilz.Constants.PlayerConstants.*;

public class GamePanel extends JPanel {

    private final MouseInputs mouseInputs;
    private BufferedImage image;
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 20;
    private int playerAction = IDLE;

    private float xDelta = 100, yDelta = 100;

    public GamePanel() {
        mouseInputs = new MouseInputs(this);

        importImg();
        loadAnimations();

        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    //Images and Animations
    private void importImg() {
        try {
            image = ImageIO.read(new File("resources/player_sprites.png"));
        } catch (IOException e) {
            System.out.println("Error loading player image at: ");
            e.printStackTrace();
        }
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][6];
        for(int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    //Window
    private void setPanelSize() {
        setPreferredSize(new Dimension(1280, 800));
    }

    //Movement
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

        updateAnimationTick();


        g.drawImage(animations[playerAction][animationIndex], (int) xDelta, (int) yDelta, 128, 80,null);
    }

    private void updateAnimationTick() {
        if(++animationTick >= animationSpeed) {
            animationTick = 0;
            if(++animationIndex >= 6) {
                animationIndex = 0;
            }
        }
    }
}

package game;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;

public class GamePanel extends JPanel {

    private final MouseInputs mouseInputs;
    private BufferedImage image;
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 20;
    private int playerAction = IDLE;
    private boolean isMoving = false;

    private float xDelta = 100, yDelta = 100;
    private int playerDir = -1;

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

    private void setAnimation() {
        if(isMoving) playerAction = RUNNING;
        else playerAction = IDLE;
    }

    //Window
    private void setPanelSize() {
        setPreferredSize(new Dimension(1280, 800));
    }

    //Movement
    public void setDirection(int direction) {
        this.playerDir = direction;
        isMoving = true;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void updatePosition() {
        if(isMoving) {
            switch (playerDir) {
                case LEFT -> xDelta-=5;
                case UP -> yDelta-=5;
                case RIGHT -> xDelta+=5;
                case DOWN -> yDelta+=5;
            }
        }
    }

    public void paint(Graphics g) {
        super.paintComponent(g);

        updateAnimationTick();

        setAnimation();
        updatePosition();
        g.drawImage(animations[playerAction][animationIndex],
                (int) xDelta, (int) yDelta, 256, 160,null);
    }
    private void updateAnimationTick() {
        if(++animationTick >= animationSpeed) {
            animationTick = 0;
            if(++animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }
    }
}

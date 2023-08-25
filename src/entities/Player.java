package entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static utilz.Constants.Directions.*;
import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 20;
    private int playerAction = IDLE;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private boolean left, up, right, down;
    private float playerSpeed = 2.0f;



    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex],
                (int) x, (int) y, 256, 160,null);
    }

    //Movement
    public void updatePosition() {

        isMoving = false;

        if(left && !right) {
            x -= playerSpeed;
            isMoving = true;
        } else if (right && !left) {
            x += playerSpeed;
            isMoving = true;
        }

        if(up && !down) {
            y -= playerSpeed;
            isMoving = true;
        } else if(down && !up) {
            y += playerSpeed;
            isMoving = true;
        }
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }


    //Animations and Images
    private void setAnimation() {
        int startAnimation = playerAction;

        if(isMoving) playerAction = RUNNING;
        else playerAction = IDLE;

        if(isAttacking) playerAction = ATTACK_1;

        if (startAnimation != playerAction) resetAnimationTicks();
    }

    private void updateAnimationTick() {
        if(++animationTick >= animationSpeed) {
            animationTick = 0;
            if(++animationIndex >= GetSpriteAmount(playerAction)) {
                animationIndex = 0;
                isAttacking = false;
            }
        }
    }

    public void resetAnimationTicks() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void loadAnimations() {

        try {
            BufferedImage image = ImageIO.read(new File("resources/player_sprites.png"));

            animations = new BufferedImage[9][6];
            for(int j = 0; j < animations.length; j++) {
                for (int i = 0; i < animations[j].length; i++) {
                    animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
                }
            }


        } catch (IOException e) {
            System.out.println("Error loading player image at: ");
            e.printStackTrace();
        }
    }

    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }
}

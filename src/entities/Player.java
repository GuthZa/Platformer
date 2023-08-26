package entities;

import game.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 20;
    private int playerAction = IDLE;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private boolean left, up, right, down;
    private float playerSpeed = Game.SCALE;
    private int[][] levelData;
    private final float xDrawOffSet = 21 * Game.SCALE;
    private final float yDrawOffSet = 4 * Game.SCALE;
    private static final float PLAYER_HITBOX_WIDTH = 20 * Game.SCALE;
    private static final float PLAYER_HITBOX_HEIGHT = 28 * Game.SCALE;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT);
    }

    public void update() {
        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    //Draw player
    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex],
                (int) (hitBox.x - xDrawOffSet), (int) (hitBox.y - yDrawOffSet),
                width, height,null);
        drawHitBox(g);
    }

    //Movement
    public void updatePosition() {
        isMoving = false;
        if(!left && !right && !up && !down) return;

        float xSpeed = 0, ySpeed = 0;

        if(left && !right) {
            xSpeed = -playerSpeed;
        } else if (right && !left) {
            xSpeed = playerSpeed;
        }

        if(up && !down) {
            ySpeed = -playerSpeed;
        } else if(down && !up) {
            ySpeed = playerSpeed;
        }

//        if(CanMoveHere(x+xSpeed, y+ySpeed, width, height, levelData)) {
//            this.x += xSpeed;
//            this.y += ySpeed;
//            this.isMoving = true;
//        }

        if(CanMoveHere(hitBox.x + xSpeed, hitBox.y + ySpeed,
                hitBox.width, hitBox.height, levelData)) {
            hitBox.x += xSpeed;
            hitBox.y += ySpeed;
            this.isMoving = true;
        }
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

        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[9][6];
        for(int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    //Data
    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
    }
    public void resetDirBooleans() {
        left = false;
        up = false;
        right = false;
        down = false;
    }


    //Getters and Setters
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
}

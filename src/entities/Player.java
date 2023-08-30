package entities;

import game.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {

    //Animations && Movement
    private BufferedImage[][] animations;
    private int animationTick, animationIndex;
    private final int animationSpeed = 20;
    private int playerAction = IDLE;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private boolean left, right, jump;
    private float playerSpeed = Game.SCALE;

    //Jumping & Gravity
    private float airSpeed = 0f;
    private final float gravity = 0.04f * Game.SCALE;
    private final float jumpSpeed = -2.25f * Game.SCALE;
    private final float fallSpeedAfterCollision = 0.5f * Game.SCALE;
    private boolean inAir = false;

    //Level data and player settings
    private int[][] levelData;
    private final float xDrawOffSet = 21 * Game.SCALE;
    private final float yDrawOffSet = 4 * Game.SCALE;
    private static final int PLAYER_HITBOX_WIDTH = (int) (20 * Game.SCALE);
    private static final int PLAYER_HITBOX_HEIGHT = (int) (27 * Game.SCALE);

    //Status Bar UI
    private BufferedImage statusBarImage;

    private final int statusBarWidth = (int) (192 * Game.SCALE);
    private final int statusBarHeight = (int) (58 * Game.SCALE);
    private final int statusBarX = (int) (10 * Game.SCALE);
    private final int statusBarY = (int) (10 * Game.SCALE);

    private final int healthBarWidth = (int) (150 * Game.SCALE);
    private final int healthBarHeight = (int) (4 * Game.SCALE);
    private final int healthBarX = (int) (34 * Game.SCALE);
    private final int healthBarY = (int) (14 * Game.SCALE);

    //Player stats
    private int maxHealth = 100;
    private int currentHealth = 40;
    private int healthWidth = healthBarWidth;

    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initHitBox(x, y, PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT);
    }

    public void update() {
        updateHealthBar();

        updatePosition();
        updateAnimationTick();
        setAnimation();
    }

    //Draw player
    public void render(Graphics g, int levelOffSet) {
        g.drawImage(animations[playerAction][animationIndex],
                (int) (hitBox.x - xDrawOffSet) - levelOffSet, (int) (hitBox.y - yDrawOffSet),
                width, height,null);

        drawUI(g);

        //For Debugging
//        drawHitBox(g, levelOffSet);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImage, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);
    }

    //Player Stats
    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    //Movement
    public void updatePosition() {
        isMoving = false;
        if(jump) jump();
        if(((!left && !right) || (left && right)) && !inAir) return;

        float xSpeed = 0;

        if (left) xSpeed -= playerSpeed;
        if (right) xSpeed += playerSpeed;

        if(!inAir && IsEntityOnFloor(hitBox, levelData)) inAir = true;

        if(inAir) {
            if(CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, levelData)) {
                hitBox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
                if(airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        } else updateXPos(xSpeed);
        isMoving = true;
    }

    private void jump() {
        if (inAir) return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitBox.x + xSpeed, hitBox.y,
                hitBox.width, hitBox.height, levelData)) {
            hitBox.x += xSpeed;
        } else {
            hitBox.x = GetEntityXPosNextToWall(hitBox, xSpeed);
        }
    }

    //Animations and Images
    private void setAnimation() {
        int startAnimation = playerAction;

        if(isMoving) playerAction = RUNNING;
        else playerAction = IDLE;

        if(isAttacking) playerAction = ATTACK_1;

        if(inAir && airSpeed < 0) playerAction = JUMP;
        else if(inAir && airSpeed > 0) playerAction = FALLING;

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

        statusBarImage = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    //Data
    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        if(IsEntityOnFloor(hitBox, levelData)) inAir = true;
    }
    public void resetDirBooleans() {
        left = false;
        right = false;
        isMoving = false;
    }


    //Getters and Setters
    public void setAttacking(boolean attacking) { isAttacking = attacking; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setJump(boolean jump) { this.jump = jump; }
}

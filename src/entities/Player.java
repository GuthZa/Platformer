package entities;

import game.Game;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;

public class Player extends Entity {
    private Playing playing;

    //Animations && Movement
    private BufferedImage[][] animations;
    private int animationTick, animationIndex;
    private final int animationSpeed = 20;
    private int playerAction = IDLE;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private boolean left, right, jump;
    private float playerSpeed = Game.SCALE;
    private int flipX = 0;
    private int flipWidth = 1;

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
    private int currentHealth = maxHealth;
    private int healthWidth = healthBarWidth;

    //Attack hitBox
    private Rectangle2D.Float attackHitBox;
    private boolean attackChecked;

    public Player(float x, float y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        loadAnimations();
        initHitBox(x, y, PLAYER_HITBOX_WIDTH, PLAYER_HITBOX_HEIGHT);
        initAttackHitBox();
    }

    //inits
    private void loadAnimations() {

        BufferedImage image = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

        animations = new BufferedImage[7][8];
        for(int j = 0; j < animations.length; j++) {
            for (int i = 0; i < animations[j].length; i++) {
                animations[j][i] = image.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

        statusBarImage = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
    }

    private void initAttackHitBox() {
        attackHitBox = new Rectangle2D.Float(x, y, (int) (20 * Game.SCALE), (int) (20 * Game.SCALE));
    }

    public void update() {
        updateHealthBar();
        if(currentHealth <= 0) {
            playing.setGameOver(true);
            return;
        }
        updateAttackHitBox();

        updatePosition();
        if(isAttacking)
            checkAttack();
        updateAnimationTick();
        setAnimation();
    }

    //Draw player
    public void render(Graphics g, int levelOffSet) {
        g.drawImage(animations[playerAction][animationIndex],
                (int) (hitBox.x - xDrawOffSet) - levelOffSet + flipX,
                (int) (hitBox.y - yDrawOffSet),
                width * flipWidth, height,null);

        drawUI(g);

        //For Debugging
//        drawHitBox(g, levelOffSet);
//        drawAttackHitBox(g, levelOffSet);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImage, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
        g.setColor(Color.RED);
        g.fillRect(healthBarX + statusBarX, healthBarY + statusBarY, healthWidth, healthBarHeight);
    }

    private void drawAttackHitBox(Graphics g, int xLevelOffSet) {
        g.setColor(Color.red);
        g.drawRect((int) attackHitBox.x - xLevelOffSet, (int) attackHitBox.y, (int) attackHitBox.width, (int) attackHitBox.height);
    }

    //Player Stats
    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float)maxHealth) * healthBarWidth);
    }

    //Combat
    private void updateAttackHitBox() {
        if (right) {
            attackHitBox.x = hitBox.x + hitBox.width + (int) (Game.SCALE * 10);
        } else if (left) {
            attackHitBox.x = hitBox.x - hitBox.width - (int) (Game.SCALE * 10);
        }
        attackHitBox.y = hitBox.y + (Game.SCALE * 10);
    }

    private void checkAttack() {
        if(attackChecked || animationIndex != 1)
            return;
        attackChecked = true;
        playing.checkEnemyHit(attackHitBox);
    }

    //Movement
    public void updatePosition() {
        isMoving = false;
        if(jump) jump();
        if(((!left && !right) || (left && right)) && !inAir) return;

        float xSpeed = 0;

        if (left) {
            xSpeed -= playerSpeed;
            flipX = width;
            flipWidth = -1;
        }
        if (right) {
            xSpeed += playerSpeed;
            flipX = 0;
            flipWidth = 1;
        }

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

    public void changeHealth(int healthToChange) {
        currentHealth += healthToChange;

        if (currentHealth <= 0) {
            currentHealth = 0;
            //gameOver()
        } else if (currentHealth > maxHealth)
            currentHealth = maxHealth;
    }

    //Animations and Images
    private void setAnimation() {
        int startAnimation = playerAction;

        if(isMoving) playerAction = RUNNING;
        else playerAction = IDLE;

        if(isAttacking && !inAir) {
            playerAction = ATTACK;
            if(startAnimation!=ATTACK) {
                animationIndex = 0;
                animationTick = 1;
                return;
            }
        }

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
                attackChecked = false;
            }
        }
    }

    public void resetAnimationTicks() {
        animationTick = 0;
        animationIndex = 0;
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
    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        isAttacking = false;
        isMoving = false;
        playerAction = IDLE;
        currentHealth = maxHealth;

        hitBox.x = x;
        hitBox.y = y;

        if (!IsEntityOnFloor(hitBox, levelData))
            inAir = true;

    }


    //Getters and Setters
    public void setAttacking(boolean attacking) { isAttacking = attacking; }
    public void setLeft(boolean left) { this.left = left; }
    public void setRight(boolean right) { this.right = right; }
    public void setJump(boolean jump) { this.jump = jump; }
}

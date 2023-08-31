package entities;

import game.Game;
import utilz.LoadSave;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Enemy.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {

    protected int animationIndex, enemyState, enemyType;
    protected int animationTick;
    protected final int animationSpeed = 25;
    protected boolean firstUpdate = true;
    protected boolean inAir;
    protected float fallSpeed;
    protected final float gravity = 0.04f * Game.SCALE;
    protected float walkSpeed = 0.3f * Game.SCALE;
    protected int walkingDirection = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;

    //Enemy stats
    protected int maxHealth;
    protected int currentHealth;
    protected int damage;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        damage = GetEnemyDamage(enemyType);
    }

    //Animation
    protected void updateAnimationTick() {
        if(++animationTick >= animationSpeed) {
            animationTick = 0;
            if(++animationIndex >= GetSpriteAmount(enemyType, enemyState)) {
                animationIndex = 0;
                switch (enemyState) {
                    case ATTACK, HIT -> enemyState = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    //Helpful methods
    protected void newState(int enemyState) {
        this.enemyState = enemyState;
        animationTick = 0;
        animationIndex = 0;
    }

    protected void firstUpdateCheck(int[][] levelData) {
        if (IsEntityOnFloor(hitBox, levelData))
            inAir = true;
        firstUpdate = false;
    }

    //Checks
    protected boolean canSeePlayer(int[][] levelData, Player player) {
        int playerTileY = (int) (player.getHitBox().y / Game.TILES_SIZE);

        return playerTileY == tileY &&
                isPlayerInRange(player) &&
                IsSightClear(levelData, hitBox, player.hitBox, tileY);
     }

    protected boolean isPlayerInRange(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance * 5;
    }

    protected boolean isPlayerCloseForAttack(Player player) {
        int absValue = (int) Math.abs(player.hitBox.x - hitBox.x);
        return absValue <= attackDistance;
    }

    protected void checkEnemyHit(Player player, Rectangle2D.Float attackHitBox) {
        if(attackHitBox.intersects(player.getHitBox()))
            player.changeHealth(-GetEnemyDamage(enemyType));
        attackChecked = true;
    }

    //Updates
    protected void updateInAir(int[][] levelData) {
        if (CanMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, levelData)) {
            hitBox.y += fallSpeed;
            fallSpeed += gravity;
        } else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            tileY = (int) hitBox.y / Game.TILES_SIZE;
        }
    }

    //Combat
    public void hit(int amount) {
        currentHealth -= amount;
        if(currentHealth <= 0)
            newState(DEAD);
        else
            newState(HIT);
    }

    //Movement
    protected void move(int[][] levelData) {
        float xSpeed;

        if (walkingDirection == LEFT) {
            xSpeed = -walkSpeed;
        } else {
            xSpeed = walkSpeed;
        }

        if (CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, levelData))
            if (IsFloor(hitBox, xSpeed, levelData)) {
                hitBox.x += xSpeed;
                return;
            }
        changeWalkDirection();
    }

    protected void changeWalkDirection() {
        if(walkingDirection == LEFT)
            walkingDirection = RIGHT;
        else
            walkingDirection = LEFT;
    }

    protected void turnTowardsPlayer(Player player) {
        if (player.hitBox.x > hitBox.x)
            walkingDirection = RIGHT;
        else
            walkingDirection = LEFT;
    }

    //Getters and Setters
    public int getEnemyState() {
        return enemyState;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public boolean isActive() {
        return active;
    }
}

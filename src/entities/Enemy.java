package entities;

import game.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.*;
import static utilz.Constants.Enemy.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {

    protected int enemyType;
    protected boolean firstUpdate = true;
    protected int walkingDirection = LEFT;
    protected int tileY;
    protected float attackDistance = Game.TILES_SIZE;
    protected boolean active = true;
    protected boolean attackChecked;
    protected boolean dying;

    //Enemy stats
    protected int damage;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        this.state = IDLE;
        this.walkSpeed = 0.35f * Game.SCALE;
        maxHealth = GetMaxHealth(enemyType);
        currentHealth = maxHealth;
        damage = GetEnemyDamage(enemyType);
    }

    //Animation
    protected void updateAnimationTick() {
        if(++animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            if(++animationIndex >= GetSpriteAmount(enemyType, state)) {
                animationIndex = 0;
                switch (state) {
                    case ATTACK, HIT -> state = IDLE;
                    case DEAD -> active = false;
                }
            }
        }
    }

    //Helpful methods
    protected void newState(int enemyState) {
        this.state = enemyState;
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
        if (CanMoveHere(hitBox.x, hitBox.y + airSpeed, hitBox.width, hitBox.height, levelData)) {
            hitBox.y += airSpeed;
            airSpeed += GRAVITY;
        } else {
            inAir = false;
            hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed);
            tileY = (int) hitBox.y / Game.TILES_SIZE;
        }
    }

    //Combat
    public void hit(int amount) {
        currentHealth -= amount;
        if(!dying) {
            if (currentHealth <= 0)
                newState(DEAD);
            else
                newState(HIT);
            dying = true;
        }
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

    public void resetEnemy() {
        hitBox.x = x;
        hitBox.y = y;
        currentHealth = maxHealth;
        firstUpdate = true;
        newState(IDLE);
        active = true;
        airSpeed = 0;
        dying = false;
    }

    //Getters and Setters
    public boolean isActive() {
        return active;
    }
}

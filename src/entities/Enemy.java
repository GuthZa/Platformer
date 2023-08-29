package entities;

import game.Game;

import java.awt.*;

import static utilz.Constants.Enemy.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;

public abstract class Enemy extends Entity {

    private int animationIndex, enemyState, enemyType;
    private int animationTick;
    private final int animationSpeed = 25;
    private boolean firstUpdate = true;
    private boolean inAir;
    private float fallSpeed;
    private final float gravity = 0.04f * Game.SCALE;
    private float walkSpeed = 0.3f * Game.SCALE;
    private int walkingDirection = LEFT;

    public Enemy(float x, float y, int width, int height, int enemyType) {
        super(x, y, width, height);
        this.enemyType = enemyType;
        initHitBox(x, y, width, height);
    }

    private void updateAnimationTick() {
        if(++animationTick >= animationSpeed) {
            animationTick = 0;
            if(++animationIndex >= GetSpriteAmount(enemyType, enemyState))
                animationIndex = 0;
        }
    }

    public void update(int[][] levelData) {
        updateMove(levelData);
        updateAnimationTick();
    }

    public void draw(Graphics g) {

    }

    private void updateMove(int[][] levelData) {
        if(firstUpdate) {
            if (IsEntityOnFloor(hitBox, levelData))
                inAir = true;
            firstUpdate = false;
        }

        if(inAir) {
            if (CanMoveHere(hitBox.x, hitBox.y + fallSpeed, hitBox.width, hitBox.height, levelData)) {
                hitBox.y += fallSpeed;
                fallSpeed += gravity;
            } else {
                inAir = false;
                hitBox.y = GetEntityYPosUnderRoofOrAboveFloor(hitBox, fallSpeed);
            }
        } else {
            switch (enemyState) {
                case IDLE -> enemyState = RUNNING;
                case RUNNING -> {
                    float xSpeed;

                    if(walkingDirection == LEFT) {
                        xSpeed = -walkSpeed;
                    } else {
                        xSpeed = walkSpeed;
                    }

                    if(CanMoveHere(hitBox.x + xSpeed, hitBox.y, hitBox.width, hitBox.height, levelData))
                        if(IsFloor(hitBox, xSpeed, levelData)) {
                            hitBox.x += xSpeed;
                            return;
                        }
                    changeWalkDirection();
                }
            }
        }
    }

    private void changeWalkDirection() {
        if(walkingDirection == LEFT)
            walkingDirection = RIGHT;
        else
            walkingDirection = LEFT;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}

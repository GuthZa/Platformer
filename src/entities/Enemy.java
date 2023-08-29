package entities;

import java.awt.*;

import static utilz.Constants.Enemy.*;

public abstract class Enemy extends Entity {

    private int animationIndex, enemyState, enemyType;
    private int animationTick;
    private final int animationSpeed = 25;

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

    public void update() {
        updateAnimationTick();
    }

    public void draw(Graphics g) {

    }

    public int getAnimationTick() {
        return animationTick;
    }

    public int getEnemyState() {
        return enemyState;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }
}

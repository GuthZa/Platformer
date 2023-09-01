package entities;

import game.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    //Animations and drawing
    protected float x, y;
    protected int width, height;
    protected int animationTick, animationIndex;
    protected int state;
    protected Rectangle2D.Float hitBox;

    //Jumping and Gravity
    protected boolean inAir;

    protected float airSpeed = 0f;
    protected float walkSpeed;

    //Stats
    protected int maxHealth;
    protected int currentHealth;

    //Attack hitBox
    protected Rectangle2D.Float attackHitBox;


    public Entity(float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE) , (int) (height * Game.SCALE));
    }

    protected void drawHitBox(Graphics g, int xLevelOffSet) {
        //For debugging
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffSet, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    protected void drawAttackHitBox(Graphics g, int xLevelOffSet) {
        g.setColor(Color.red);
        g.drawRect((int) attackHitBox.x - xLevelOffSet, (int) attackHitBox.y, (int) attackHitBox.width, (int) attackHitBox.height);
    }

    public Rectangle2D.Float getHitBox() {return hitBox; }

    public int getState() { return state; }

    public int getAnimationIndex() { return animationIndex; }
}

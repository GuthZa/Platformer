package objects;

import game.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.ANIMATION_SPEED;
import static utilz.Constants.ObjectConstants.*;

public class GameObject {
    protected int x, y, objectType;
    protected Rectangle2D.Float hitBox;
    protected boolean doAnimation, active = true;
    protected int animationTick, animationIndex;
    protected int xDrawOffSet, yDrawOffSet;

    public GameObject(int x, int y, int objectType) {
        this.x = x;
        this.y = y;
        this.objectType = objectType;
    }

    protected void initHitBox(int width, int height) {
        hitBox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE) , (int) (height * Game.SCALE));
    }

    public void  drawHitBox(Graphics g, int xLevelOffSet) {
        //For debugging
        g.setColor(Color.PINK);
        g.drawRect((int) hitBox.x - xLevelOffSet, (int) hitBox.y, (int) hitBox.width, (int) hitBox.height);
    }

    //Animation
    protected void updateAnimationTick() {
        if(++animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            if(++animationIndex >= GetSpriteAmount(objectType)) {
                animationIndex = 0;
                if(objectType == BARREL || objectType == BOX) {
                    doAnimation = false;
                    active = false;
                } else if (objectType == CANNON_LEFT || objectType == CANNON_RIGHT) {
                    doAnimation = false;
                }
            }
        }
    }

    public void reset() {
        animationIndex = 0;
        animationTick = 0;
        active = true;
        doAnimation = !(
                objectType == BARREL ||
                objectType == BOX ||
                objectType == CANNON_LEFT ||
                objectType == CANNON_RIGHT
        );
    }

    //Getters and Setter
    public int getObjectType() {
        return objectType;
    }

    public Rectangle2D.Float getHitBox() {
        return hitBox;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public boolean isDoingAnimation() { return doAnimation; }
    public void setDoAnimation(boolean doAnimation) {this.doAnimation = doAnimation; }
    public int getXDrawOffSet() {
        return xDrawOffSet;
    }

    public int getYDrawOffSet() {
        return yDrawOffSet;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public int getAnimationTick() { return animationTick; }
}

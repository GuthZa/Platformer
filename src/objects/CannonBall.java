package objects;

import game.Game;

import java.awt.geom.Rectangle2D;

import static utilz.Constants.Projectiles.*;

public class CannonBall {
    private Rectangle2D.Float hitBox;
    private int direction;
    private boolean active = true;

    public CannonBall(int x, int y, int direction) {
        int xOffSet = (int) (-3 * Game.SCALE);
        int yOffSet = (int) (5 * Game.SCALE);

        if(direction == 1)
            xOffSet = (int) (29 * Game.SCALE);

        hitBox = new Rectangle2D.Float(x + xOffSet, y + yOffSet, CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT);
        this.direction = direction;


    }

    public void updatePosition() {
        hitBox.x += direction * SPEED;
    }

    public void setPosition(int x, int y) {
        hitBox.x = x;
        hitBox.y = y;
    }

    public Rectangle2D.Float getHitBox() { return hitBox; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}

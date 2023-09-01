package objects;

import game.Game;

public class Potion extends GameObject {

    private float hoverOffset;
    private int maxHoverOffset, hoverDirection = 1;

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitBox(7, 14);
        xDrawOffSet = (int) (3 * Game.SCALE);
        yDrawOffSet = (int) (2 * Game.SCALE);

        maxHoverOffset = (int) (10 * Game.SCALE);
    }

    public void update() {
        updateAnimationTick();
        updateHover();
    }

    private void updateHover() {
        //increase for faster, decrease for slower
        hoverOffset += (0.075f * Game.SCALE * hoverDirection);
        if (hoverOffset >= maxHoverOffset) hoverDirection *= -1;
        else if (hoverOffset < 0) hoverDirection = 1;

        hitBox.y = y + hoverOffset;
    }
}

package objects;

import game.Game;

public class Spike extends  GameObject {
    public Spike(int x, int y, int objectType) {
        super(x, y, objectType);
        initHitBox(32, 16);
        xDrawOffSet = 0;
        yDrawOffSet = (int) (16 * Game.SCALE);
        hitBox.y += yDrawOffSet;
    }

    private void update() { updateAnimationTick(); }
}

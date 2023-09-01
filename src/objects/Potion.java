package objects;

import game.Game;

public class Potion extends GameObject {

    public Potion(int x, int y, int objectType) {
        super(x, y, objectType);
        doAnimation = true;
        initHitBox(7, 14);
        xDrawOffSet = (int) (3 * Game.SCALE);
        yDrawOffSet = (int) (2 * Game.SCALE);

    }

    public void update() {
        updateAnimationTick();
    }
}

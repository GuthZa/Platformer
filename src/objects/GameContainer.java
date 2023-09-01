package objects;

import game.Game;

import static utilz.Constants.ObjectConstants.*;

public class GameContainer extends GameObject {

    public GameContainer(int x, int y, int objectType) {
        super(x, y, objectType);
        createHitBox();
    }

    private void createHitBox() {
        if(objectType == BOX) {
            initHitBox(25, 18);
            xDrawOffSet = (int) (7 * Game.SCALE);
            yDrawOffSet = (int) (12 * Game.SCALE);
        } else {
            initHitBox(23, 25);
            xDrawOffSet = (int) (8 * Game.SCALE);
            yDrawOffSet = (int) (5 * Game.SCALE);
        }

        hitBox.y += yDrawOffSet + (int) (Game.SCALE * 2);
        hitBox.x += xDrawOffSet / 2f;
    }

    public void update() { if(doAnimation) updateAnimationTick(); }
}

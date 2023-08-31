package entities;

import game.Game;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.Enemy.*;

public class Crabby extends Enemy {

    //Attack hitBox
    private int attackBoxOffSet;

    public Crabby(float x, float y) {
        super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY);
        initHitBox(22, 19);
        initAttackHitBox();
    }

    private void initAttackHitBox() {
        attackHitBox = new Rectangle2D.Float(x, y, (int) (82 * Game.SCALE), (int) (19 * Game.SCALE));
        attackBoxOffSet = (int) (Game.SCALE * 30);
    }

    public void update(int[][] levelData, Player player) {
        updateBehaviour(levelData, player);
        updateAnimationTick();
        updateAttackHitBox();
    }

    private void updateBehaviour(int[][] levelData, Player player) {
        if(firstUpdate)
            firstUpdateCheck(levelData);

        if(inAir)
            updateInAir(levelData);
        else {
            switch (state) {
                case IDLE -> newState(RUNNING);
                case RUNNING -> {
                    if(canSeePlayer(levelData, player)) {
                        turnTowardsPlayer(player);
                        if (isPlayerCloseForAttack(player))
                            newState(ATTACK);
                    }
                    move(levelData);
                }
                case ATTACK -> {
                    if (animationIndex == 0) attackChecked = false;
                    if (animationIndex == 3 && !attackChecked)
                        checkEnemyHit(player, attackHitBox);
                }
//                case HIT -> ;
            }
        }
    }

    private void updateAttackHitBox() {
        attackHitBox.x = hitBox.x - attackBoxOffSet;
        attackHitBox.y = hitBox.y;
    }
    public int flipX() {
        return (walkingDirection == RIGHT) ? width : 0;
    }

    public int flipWidth() {
        return  (walkingDirection == RIGHT) ? -1 : 1;
    }
}

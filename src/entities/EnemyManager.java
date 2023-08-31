package entities;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.Enemy.*;
public class EnemyManager {

    private Playing playing;
    private BufferedImage[][] crabbyArray;
    private ArrayList<Crabby> crabbies = new ArrayList<>();

    public EnemyManager(Playing playing) {
        this.playing = playing;
        loadEnemyImages();
    }

    private void loadEnemyImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        crabbyArray = new BufferedImage[5][9];

        for (int j = 0; j < crabbyArray.length; j++)
            for (int i = 0; i < crabbyArray[j].length; i++)
                crabbyArray[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT,
                        CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
    }

    public void loadEnemies(Level level) {
        crabbies = level.getCrabs();
    }

    public void update(int[][] levelData, Player player) {
        boolean isAnyActive = false;
        for (Crabby crabby: crabbies)
            if(crabby.isActive()) {
                crabby.update(levelData, player);
                isAnyActive = true;
            }
        if(!isAnyActive)
            playing.setLevelCompleted(true);
    }

    public void draw(Graphics g, int xLevelOffSet) {
        drawCrabs(g, xLevelOffSet);
    }

    private void drawCrabs(Graphics g, int xLevelOffSet) {
        for (Crabby crabby: crabbies) {
            if (crabby.isActive())
                g.drawImage(crabbyArray[crabby.getState()][crabby.getAnimationIndex()],
                    (int) crabby.getHitBox().x - xLevelOffSet- CRABBY_DRAW_OFFSET_X + crabby.flipX(),
                    (int) crabby.getHitBox().y - CRABBY_DRAW_OFFSET_Y,
                    CRABBY_WIDTH * crabby.flipWidth(), CRABBY_HEIGHT, null);

            //For debugging
//            crabby.drawHitBox(g, xLevelOffSet);
//            crabby.drawAttackBox(g, xLevelOffSet);
        }
    }

    public void checkEnemyHit(Rectangle2D.Float attackHitBox) {
        for (Crabby crabby : crabbies) {
            if(crabby.isActive() && attackHitBox.intersects(crabby.getHitBox())) {
                crabby.hit(10);
                return;
            }
        }
    }

    public void resetAllEnemies() {
        for (Crabby crabby : crabbies)
            crabby.resetEnemy();
    }
}

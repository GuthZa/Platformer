package entities;

import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
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
        addEnemies();
    }

    private void loadEnemyImages() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
        crabbyArray = new BufferedImage[5][9];

        for (int j = 0; j < crabbyArray.length; j++) {
            for (int i = 0; i < crabbyArray[j].length; i++) {
                crabbyArray[j][i] = temp.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT,
                        CRABBY_WIDTH_DEFAULT, CRABBY_HEIGHT_DEFAULT);
            }
        }
    }

    public void update() {
        for (Crabby crabby: crabbies) {
            crabby.update();
        }
    }

    public void draw(Graphics g, int xLevelOffSet) {
        drawCrabs(g, xLevelOffSet);
    }

    private void drawCrabs(Graphics g, int xLevelOffSet) {
        for (Crabby crabby: crabbies) {
            g.drawImage(crabbyArray[crabby.getEnemyState()][crabby.getAnimationIndex()],
                    (int) crabby.getHitBox().x - xLevelOffSet, (int) crabby.getHitBox().y,
                    CRABBY_WIDTH, CRABBY_HEIGHT, null);
        }
    }

    private void addEnemies() {
        crabbies = LoadSave.GetCrabs();
        System.out.println("Crab size: " + crabbies.size());
    }
}

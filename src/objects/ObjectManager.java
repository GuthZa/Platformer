package objects;

import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private final Playing playing;
    private BufferedImage[][] potionImages, containerImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;

    public ObjectManager(Playing playing) {
        this.playing = playing;

        loadImages();
    }

    private void loadImages() {
        BufferedImage potionSprites = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImages = new BufferedImage[2][7];

        for (int j = 0; j < potionImages.length; j++)
            for (int i = 0; i < potionImages[0].length; i++)
                potionImages[j][i] = potionSprites.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprites = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImages = new BufferedImage[2][8];

        for (int j = 0; j < containerImages.length; j++)
            for (int i = 0; i < containerImages[0].length; i++)
                containerImages[j][i] = containerSprites.getSubimage(40 * i, 30 * j, 40, 30);
    }

    public void loadObjects(Level newLevel) {
        potions = newLevel.getPotions();
        containers = newLevel.getContainers();
    }

    public void checkObjectTouched(Rectangle2D.Float hitBox) {
        potions.stream().filter(Potion::isActive).forEach(potion -> {
            if (potion.getHitBox().intersects(hitBox)) {
                potion.setActive(false);
                applyEffectToPlayer(potion);
            }
        });

    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjectType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float hitBox) {
        containers.stream().filter(GameContainer::isActive).forEach(container -> {
            if(container.getHitBox().intersects(hitBox)) {
                container.setDoAnimation(true);
                int type = 0;
                if (container.getObjectType() == BARREL) type = 1;
                potions.add(new Potion((int) (container.getHitBox().x + container.getHitBox().width / 2),
                        (int) (container.getHitBox().y + container.getHitBox().height / 4), type));
            }
        });
    }

    public void update() {
        potions.stream().filter(Potion::isActive).forEach(Potion::update);
        containers.stream().filter(GameContainer::isActive).forEach(GameContainer::update);
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);

        //Debugging
//        potions.forEach(potion -> potion.drawHitBox(g, xLevelOffset));
//        containers.forEach(container -> container.drawHitBox(g, xLevelOffset));
    }

    public void drawPotions(Graphics g, int xLevelOffSet) {
        potions.stream().filter(Potion::isActive).forEach(potion -> {
            int type = 0;
            if(potion.getObjectType() == RED_POTION) type = 1;
            g.drawImage(potionImages[type][potion.getAnimationIndex()],
                    (int)(potion.getHitBox().x - potion.getXDrawOffSet() - xLevelOffSet),
                    (int)(potion.getHitBox().y - potion.getYDrawOffSet()),
                    POTION_WIDTH, POTION_HEIGHT, null);
        });
    }

    public void drawContainers(Graphics g, int xLevelOffSet) {
        containers.stream().filter(GameContainer::isActive).forEach(container -> {
            int type = 0;
            if (container.getObjectType() == BARREL) type = 1;
            g.drawImage(containerImages[type][container.getAnimationIndex()],
                    (int)(container.getHitBox().x - container.getXDrawOffSet() - xLevelOffSet),
                    (int)(container.getHitBox().y - container.getYDrawOffSet()),
                    CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
        });
    }

    public void resetAllObjects() {
        potions.forEach(Potion::reset);
        containers.forEach(GameContainer::reset);
    }
}

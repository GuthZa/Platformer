package objects;

import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;

public class ObjectManager {
    private final Playing playing;
    private BufferedImage[][] potionImages, containerImages;
    private final ArrayList<Potion> potions;
    private final ArrayList<GameContainer> containers;

    public ObjectManager(Playing playing) {
        this.playing = playing;

        potions = new ArrayList<>();
        containers = new ArrayList<>();

        loadImages();

        potions.add(new Potion(300, 300, RED_POTION));
        potions.add(new Potion(400, 300, BLUE_POTION));
        containers.add(new GameContainer(500, 300, BARREL));
        containers.add(new GameContainer(600, 300, BOX));
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

    public void update() {
        potions.stream().filter(Potion::isActive).forEach(Potion::update);
//        containers.stream().filter(GameContainer::isActive).forEach(GameContainer::update);
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);

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
}

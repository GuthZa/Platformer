package objects;

import entities.Player;
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
    private BufferedImage spikeImages;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;

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

        spikeImages = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = new ArrayList<>(newLevel.getSpikes());
    }

    public void checkObjectTouched(Rectangle2D.Float hitBox) {
        potions.stream().
                filter(potion ->
                        potion.isActive() &&
                        potion.getHitBox().intersects(hitBox)).
                forEach(potion -> {
                    potion.setActive(false);
                    applyEffectToPlayer(potion);
                });
    }

    public void checkSpikeTouched(Player player) {
        spikes.stream().
                filter(spike -> spike.isActive() && spike.getHitBox().intersects(player.getHitBox())).
                forEach(spike -> player.kill());
    }

    public void applyEffectToPlayer(Potion potion) {
        if (potion.getObjectType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

    public void checkObjectHit(Rectangle2D.Float hitBox) {
        containers.stream().filter(container ->
                        !container.isDoingAnimation() &&
                        container.isActive() &&
                        container.getHitBox().intersects(hitBox)).
                forEach(container ->
                {
                    container.setDoAnimation(true);
                    int type = 0;
                    if (container.getObjectType() == BARREL) type = 1;
                    potions.add(new Potion((int) (
                            container.getHitBox().x + container.getHitBox().width / 2),
                            (int) (container.getHitBox().y - container.getHitBox().height / 2),
                            type));
                });
    }

    public void update() {
        potions.stream().
                filter(Potion::isActive).
                forEach(Potion::update);
        containers.stream().
                filter(GameContainer::isActive).
                forEach(GameContainer::update);
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
        drawSpikes(g, xLevelOffset);

        //Debugging
//        potions.forEach(potion -> potion.drawHitBox(g, xLevelOffset));
//        containers.forEach(container -> container.drawHitBox(g, xLevelOffset));
    }

    public void drawPotions(Graphics g, int xLevelOffSet) {
        potions.stream().
                filter(Potion::isActive).
                forEach(potion -> {
                    int type = 0;
                    if(potion.getObjectType() == RED_POTION) type = 1;
                    g.drawImage(potionImages[type][potion.getAnimationIndex()],
                            (int)(potion.getHitBox().x - potion.getXDrawOffSet() - xLevelOffSet),
                            (int)(potion.getHitBox().y - potion.getYDrawOffSet()),
                            POTION_WIDTH, POTION_HEIGHT, null);
                });
    }

    public void drawContainers(Graphics g, int xLevelOffSet) {
        containers.stream().
                filter(GameContainer::isActive).
                forEach(container -> {
                    int type = 0;
                    if (container.getObjectType() == BARREL) type = 1;
                    g.drawImage(containerImages[type][container.getAnimationIndex()],
                            (int)(container.getHitBox().x - container.getXDrawOffSet() - xLevelOffSet),
                            (int)(container.getHitBox().y - container.getYDrawOffSet()),
                            CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
                });
    }

    public void drawSpikes(Graphics g, int xLevelOffSet) {
        spikes.stream().
                filter(Spike::isActive).
                forEach(spike ->
                    g.drawImage(spikeImages,
                            (int)(spike.getHitBox().x - xLevelOffSet),
                            (int)(spike.getHitBox().y - spike.getYDrawOffSet()),
                            SPIKE_WIDTH, SPIKE_HEIGHT, null)
                );
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());

        potions.forEach(Potion::reset);
        containers.forEach(GameContainer::reset);
    }
}

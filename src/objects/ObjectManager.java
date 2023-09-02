package objects;

import entities.Player;
import game.Game;
import gamestates.Playing;
import levels.Level;
import utilz.LoadSave;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.ObjectConstants.*;
import static utilz.Constants.Projectiles.*;
import static utilz.HelpMethods.*;

public class ObjectManager {
    private final Playing playing;
    private BufferedImage[][] potionImages, containerImages;
    private BufferedImage[] cannonImages;
    private BufferedImage spikeImages, cannonBallImage;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<CannonBall> cannonBalls = new ArrayList<>();
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;

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

        BufferedImage cannonSprites = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        cannonImages = new BufferedImage[7];
        for (int i = 0; i < cannonImages.length; i++) {
            cannonImages[i] = cannonSprites.getSubimage(i * 40, 0, 40, 26);
        }

        cannonBallImage = LoadSave.GetSpriteAtlas(LoadSave.PROJECTILE_ATLAS);
    }

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = new ArrayList<>(newLevel.getSpikes());
        cannons = new ArrayList<>(newLevel.getCannons());
        cannonBalls.clear();
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

    private boolean isPlayerInFrontOfCannon(Cannon cannon, Player player) {
        if (cannon.getObjectType() == CANNON_LEFT) {
            return (cannon.getHitBox().x > player.getHitBox().x);
        }
        if (cannon.getObjectType() == CANNON_RIGHT) {
            return (cannon.getHitBox().x < player.getHitBox().x);
        }
        return false;
    }

    private boolean isPlayerInRange(Cannon cannon, Player player) {
        int absValue = (int) Math.abs(player.getHitBox().x - cannon.getHitBox().x);
        return absValue <= Game.TILES_SIZE * 5;
    }

    public void update(int[][] levelData, Player player) {
        potions.stream().
                filter(Potion::isActive).
                forEach(Potion::update);
        containers.stream().
                filter(GameContainer::isActive).
                forEach(GameContainer::update);
        updateCannons(levelData, player);
        updateProjectiles(levelData, player);
    }

    private void updateCannons(int[][] levelData, Player player) {
        cannons.stream().
                filter(cannon ->
                        !cannon.isDoingAnimation() &&
                        cannon.getTileY() == player.getTileY() &&
                        isPlayerInRange(cannon, player) &&
                        isPlayerInFrontOfCannon(cannon, player) &&
                        CanCannonSeePlayer(levelData, player.getHitBox(), cannon.getHitBox(), cannon.getTileY())).
                forEach(this::shootCannon);

        cannons.forEach(Cannon::update);
    }
    private void shootCannon(Cannon cannon) {
        cannon.setDoAnimation(true);

        int direction = cannon.getObjectType() == CANNON_LEFT ? -1 : 1;
        cannonBalls.add(new CannonBall((int) cannon.getHitBox().x, (int) cannon.getHitBox().y, direction));
    }

    private void updateProjectiles(int[][] levelData, Player player) {
        cannonBalls.stream().
                filter(CannonBall::isActive).
                forEach(CannonBall::updatePosition);
    }

    public void draw(Graphics g, int xLevelOffset) {
        drawPotions(g, xLevelOffset);
        drawContainers(g, xLevelOffset);
        drawSpikes(g, xLevelOffset);
        drawCannons(g, xLevelOffset);
        drawProjectiles(g, xLevelOffset);

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

    public void drawCannons(Graphics g, int xLevelOffSet) {
        for (Cannon cannon :
                cannons) {
            int x = (int) (cannon.getHitBox().x - xLevelOffSet);
            int width = CANNON_WIDTH;
            if (cannon.getObjectType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImages[cannon.getAnimationIndex()],
                    x, (int) (cannon.getHitBox().y), width, CANNON_HEIGHT, null);
        }
    }

    public void drawProjectiles(Graphics g, int xLevelOffSet) {
        cannonBalls.stream().
                filter(CannonBall::isActive).
                forEach(cannonBall -> g.drawImage(
                        cannonBallImage,
                        (int) (cannonBall.getHitBox().x - xLevelOffSet),
                        (int) (cannonBall.getHitBox().y),
                        CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null)
                );
    }


    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());

        potions.forEach(Potion::reset);
        containers.forEach(GameContainer::reset);
        cannons.forEach(Cannon::reset);
    }
}

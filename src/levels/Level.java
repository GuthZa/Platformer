package levels;

import entities.Crabby;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static game.Game.*;
import static utilz.HelpMethods.*;

public class Level {

    private BufferedImage image;
    private int[][] levelData;
    private ArrayList<Crabby> crabs;
    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private ArrayList<Cannon> cannons;
    private int levelTilesWide;
    private int maxTilesOffSet;
    private int maxLevelOffSetX;
    private Point playerSpawn;

    public Level(BufferedImage image) {

        this.image = image;
        createLevelData();
        createPotions();
        createContainers();
        createSpikes();
        createEnemies();
        createCannons();
        calculateLevelOffSets();
        calculatePlayerSpawn();
    }

    private void createLevelData() {
        levelData = GetLevelData(image);
    }

    private void createPotions() { potions = GetPotions(image); }
    private void createContainers() { containers = GetContainers(image); }
    private void createSpikes() { spikes = GetSpikes(image); }
    private void createEnemies() {
        crabs = GetCrabs(image);
    }
    private void createCannons() { cannons = GetCannons(image); }

    private void calculateLevelOffSets() {
        levelTilesWide = image.getWidth();
        maxTilesOffSet = levelTilesWide - TILES_IN_WIDTH;
        maxLevelOffSetX = maxTilesOffSet * TILES_SIZE;
    }

    private void calculatePlayerSpawn() {
        playerSpawn = GetPlayerSpawn(image);
    }

    public int getSpriteIndex(int x, int y) {
        return levelData[y][x];
    }

    public int[][] getLevelData() {
        return levelData;
    }

    public int getMaxLevelOffSetX() {
        return maxLevelOffSetX;
    }

    public ArrayList<Crabby> getCrabs() {
        return crabs;
    }

    public ArrayList<Potion> getPotions() { return potions; }

    public ArrayList<Spike> getSpikes() { return spikes; }

    public ArrayList<Cannon> getCannons() { return cannons; }

    public ArrayList<GameContainer> getContainers() { return containers; }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}

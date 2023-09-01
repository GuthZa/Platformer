package levels;

import entities.Crabby;
import objects.GameContainer;
import objects.Potion;

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
    private ArrayList<GameContainer> containers;
    private int levelTilesWide;
    private int maxTilesOffSet;
    private int maxLevelOffSetX;
    private Point playerSpawn;

    public Level(BufferedImage image) {

        this.image = image;
        createLevelData();
        createPotions();
        createContainers();
        createEnemies();
        calculateLevelOffSets();
        calculatePlayerSpawn();
    }

    private void createLevelData() {
        levelData = GetLevelData(image);
    }

    private void createPotions() { potions = GetPotions(image); }
    private void createContainers() { containers = GetContainers(image); }

    private void createEnemies() {
        crabs = GetCrabs(image);
    }

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

    public ArrayList<GameContainer> getContainers() { return containers; }

    public Point getPlayerSpawn() {
        return playerSpawn;
    }
}

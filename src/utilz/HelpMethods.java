package utilz;

import entities.Crabby;
import game.Game;
import objects.Cannon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.Enemy.CRABBY;
import static utilz.Constants.ObjectConstants.*;

public class HelpMethods {

    public static int[][] GetLevelData(BufferedImage image) {
        int[][] levelData = new int[image.getHeight()][image.getWidth()];

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getRed();
                if(value >= 48) value = 0;
                levelData[j][i] = value;
            }
        }

        return levelData;
    }

    public static ArrayList<Crabby> GetCrabs(BufferedImage image) {
        ArrayList<Crabby> list = new ArrayList<>();

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();
                if(value == CRABBY)
                    list.add(new Crabby(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
            }
        }
        return list;
    }

    public static ArrayList<Potion> GetPotions(BufferedImage image) {
        ArrayList<Potion> list = new ArrayList<>();

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == RED_POTION || value == BLUE_POTION)
                    list.add(new Potion(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        }
        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage image) {
        ArrayList<GameContainer> list = new ArrayList<>();

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == BOX || value == BARREL)
                    list.add(new GameContainer(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
            }
        }
        return list;
    }

    public static ArrayList<Spike> GetSpikes(BufferedImage image) {
        ArrayList<Spike> list = new ArrayList<>();

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == SPIKE)
                    list.add(new Spike(i * Game.TILES_SIZE, j * Game.TILES_SIZE, SPIKE));
            }
        }
        return list;
    }

    public static ArrayList<Cannon> GetCannons(BufferedImage image) {
        ArrayList<Cannon> list = new ArrayList<>();

        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getBlue();
                if(value == CANNON_LEFT || value == CANNON_RIGHT) {
                    list.add(new Cannon(i * Game.TILES_SIZE, j * Game.TILES_SIZE, value));
                }
            }
        }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage image) {
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                Color color = new Color(image.getRGB(i, j));
                int value = color.getGreen();
                if(value == 100)
                    return new Point(i * Game.TILES_SIZE, j * Game.TILES_SIZE);
            }
        }
        return new Point(2 * Game.TILES_SIZE, 2 * Game.TILES_SIZE);
    }

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] levelData) {

        return !IsSolid(x, y, levelData) &&
                !IsSolid(x+width, y, levelData) &&
                !IsSolid(x, y+height, levelData) &&
                !IsSolid(x+width, y+height, levelData);

    }

    private static boolean IsSolid(float x, float y, int[][] levelData) {
        int levelMaxWidth = levelData[0].length * Game.TILES_SIZE;
        //outside game window
        if(x < 0 || x >= levelMaxWidth) return true;
        if(y < 0 || y >= Game.GAME_HEIGHT) return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        return IsTileSolid((int) xIndex, (int) yIndex, levelData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] levelData) {
        int value = levelData[yTile][xTile];

        return value >= 48 || value < 0 || value != 11;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitBox, float xSpeed) {
        int currentTile = (int)(hitBox.x / Game.TILES_SIZE);

        if(xSpeed > 0) { //right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffSet = (int)(Game.TILES_SIZE - hitBox.width);
            return tileXPos + xOffSet - 1;
        } else { //left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitBox, float airSpeed) {
        int currentTile = (int)(hitBox.y / Game.TILES_SIZE);

        if(airSpeed > 0) { //falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffSet = (int)(Game.TILES_SIZE - hitBox.height);
            return tileYPos + yOffSet - 1;
        } else { //jumping - checking roof
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitBox, int[][] levelData) {
        return !IsSolid(hitBox.x, hitBox.y + hitBox.height + 1, levelData) &&
                !IsSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, levelData);
    }

    public static boolean IsFloor(Rectangle2D.Float hitBox, float xSpeed, int[][] levelData) {
        if(xSpeed > 0)
            return IsSolid(hitBox.x + hitBox.width + xSpeed, hitBox.y + hitBox.height + 1, levelData);
        return IsSolid(hitBox.x + xSpeed, hitBox.y + hitBox.height + 1, levelData);
    }

    public static boolean IsAllTilesWalkable(int xStart, int xEnd, int y, int[][] levelData) {
        if (IsAllTilesClear(xStart, xEnd, y, levelData))
            for (int i = 0; i < xEnd - xStart; i++)
                 if (!IsTileSolid(xStart + i, y + 1, levelData))
                     return false;
        return true;
    }

    public static boolean IsSightClear(int[][] levelData, Rectangle2D.Float firstHitBox, Rectangle2D.Float secondHitBox, int tileY) {
        int firstXTile = (int) (firstHitBox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitBox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesWalkable(secondXTile, firstXTile, tileY, levelData);
        else
            return IsAllTilesWalkable(firstXTile, secondXTile, tileY, levelData);
    }

    public static boolean CanCannonSeePlayer(int[][] levelData, Rectangle2D.Float firstHitBox, Rectangle2D.Float secondHitBox, int tileY) {
        int firstXTile = (int) (firstHitBox.x / Game.TILES_SIZE);
        int secondXTile = (int) (secondHitBox.x / Game.TILES_SIZE);

        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, tileY, levelData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, tileY, levelData);
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] levelData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, levelData))
                return false;
        return true;
    }
}

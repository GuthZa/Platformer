package utilz;

import game.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {
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
        for (int i = 0; i < xEnd - xStart; i++) {
            if (IsTileSolid(xStart + i, y, levelData))
                return false;
            if (!IsTileSolid(xStart + i, y + 1, levelData))
                return false;
        }
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
}

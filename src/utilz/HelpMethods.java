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
        //outside game window
        if(x < 0 || x >= Game.GAME_WIDTH) return true;
        if(y < 0 || y >= Game.GAME_HEIGHT) return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = levelData[(int) yIndex][(int) xIndex];

        return value >= 48 || value < 0 || value != 11;
    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);

        if(xSpeed > 0) { //right
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffSet = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffSet - 1;
        } else { //left
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);

        if(airSpeed > 0) { //falling - touching floor
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffSet = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffSet - 1;
        } else { //jumping - checking roof
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        return IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData) ||
                IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, levelData);
    }
}

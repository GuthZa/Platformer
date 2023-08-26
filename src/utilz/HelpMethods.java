package utilz;

import game.Game;

public class HelpMethods {
    public static boolean CanMoveHere(float x, float y, int width, int height, int[][] levelData) {

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

        return (value >= 48 || value < 0 || value != 11);
    }
}

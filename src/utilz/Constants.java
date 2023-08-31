package utilz;

import game.Game;

public class Constants {

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class PlayerConstants {
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int JUMP = 2;
        public static final int FALLING = 3;
        public static final int ATTACK = 4;
        public static final int HIT = 5;
        public static final int DEAD = 6;

        public static int GetSpriteAmount(int player_action) {
            switch (player_action) {
                case RUNNING -> {
                    return 6;
                }
                case IDLE -> {
                    return 5;
                }
                case HIT -> {
                    return 4;
                }
                case JUMP, ATTACK -> {
                    return 3;
                }
                case DEAD -> {
                    return 8;
                }
                case FALLING -> {
                    return 1;
                }
            }
            return 1;
        }
    }

    public static class Enemy {
        //Enemy Type
        public static final int CRABBY = 0;
        public static final int IDLE = 0;
        public static final int RUNNING = 1;
        public static final int ATTACK = 2;
        public static final int HIT = 3;
        public static final int DEAD = 4;

        public static final int CRABBY_WIDTH_DEFAULT = 72;
        public static final int CRABBY_HEIGHT_DEFAULT = 32;

        public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
        public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);

        public static final int CRABBY_DRAW_OFFSET_X = (int) (26 * Game.SCALE);
        public static final int CRABBY_DRAW_OFFSET_Y = (int) (9 * Game.SCALE);

        public static int GetSpriteAmount(int enemyType, int enemyState) {
            switch (enemyType) {
                case CRABBY -> {
                    switch (enemyState) {
                        case RUNNING -> {
                            return 6;
                        }
                        case IDLE -> {
                            return 9;
                        }
                        case HIT -> {
                            return 4;
                        }
                        case ATTACK -> {
                            return 7;
                        }
                        case DEAD -> {
                            return 5;
                        }
                    }
                }
            }
            return 0;
        }
    }

    public static class UI {
        public static class Buttons {
            public static final int BUTTON_WIDTH_DEFAULT = 140;
            public static final int BUTTON_HEIGHT_DEFAULT = 56;
            public static final int BUTTON_WIDTH = (int) (BUTTON_WIDTH_DEFAULT * Game.SCALE);
            public static final int BUTTON_HEIGHT = (int) (BUTTON_HEIGHT_DEFAULT * Game.SCALE);
        }

        public static class PauseButtons {
            public static final int SOUND_BUTTON_DEFAULT_SIZE = 42;
            public static final int SOUND_BUTTON_SIZE = (int) (SOUND_BUTTON_DEFAULT_SIZE * Game.SCALE);
        }

        public static class URMButtons {
            public static final int URM_BUTTON_DEFAULT_SIZE = 56;
            public static final int URM_BUTTON_SIZE = (int) (URM_BUTTON_DEFAULT_SIZE * Game.SCALE);
        }

        public static class VolumeButtons {
            public static final int VOLUME_BUTTON_DEFAULT_WIDTH = 28;
            public static final int VOLUME_BUTTON_DEFAULT_HEIGHT = 44;
            public static final int VOLUME_SLIDER_DEFAULT_WIDTH = 215;
            public static final int VOLUME_BUTTON_WIDTH = (int) (VOLUME_BUTTON_DEFAULT_WIDTH * Game.SCALE);
            public static final int VOLUME_BUTTON_HEIGHT = (int) (VOLUME_BUTTON_DEFAULT_HEIGHT * Game.SCALE);
            public static final int VOLUME_SLIDER_WIDTH = (int) (VOLUME_SLIDER_DEFAULT_WIDTH * Game.SCALE);
        }
    }

    public static class Environment {
        public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
        public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
        public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
    }
}

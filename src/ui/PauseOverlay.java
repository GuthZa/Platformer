package ui;

import game.Game;
import gamestates.GameState;
import gamestates.Playing;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.URMButtons.*;

public class PauseOverlay {

    private Playing playing;

    private BufferedImage backgroundImage;
    private SoundButton musicButton, sfxButton;
    private UrmButton unpauseButton, replayButton, menuButton;

    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    public PauseOverlay(Playing playing) {
        this.playing = playing;
        loadBackground();
        createSoundButtons();
        createUrmButtons();
    }

    private void loadBackground() {
        backgroundImage = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
        backgroundWidth = (int) (backgroundImage.getWidth() * Game.SCALE);
        backgroundHeight = (int) (backgroundImage.getHeight() * Game.SCALE);
        backgroundX = Game.GAME_WIDTH / 2 - backgroundWidth / 2;
        backgroundY = (int) (25 * Game.SCALE);
    }

    private void createSoundButtons() {
        int soundX = (int) (450 * Game.SCALE);
        int musicY = (int) (140 * Game.SCALE);
        int sfxY = (int) (186 * Game.SCALE);

        musicButton = new SoundButton(soundX, musicY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
        sfxButton = new SoundButton(soundX, sfxY, SOUND_BUTTON_SIZE, SOUND_BUTTON_SIZE);
    }

    private void createUrmButtons() {
        int menuX = (int) (313 * Game.SCALE);
        int replayX = (int) (387 * Game.SCALE);
        int unpauseX = (int) (462 * Game.SCALE);
        int buttonY = (int) (325 * Game.SCALE);

        menuButton = new UrmButton(menuX, buttonY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 2);
        replayButton = new UrmButton(replayX, buttonY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 1);
        unpauseButton = new UrmButton(unpauseX, buttonY, URM_BUTTON_SIZE, URM_BUTTON_SIZE, 0);
    }

    public void draw(Graphics g) {
        //Background
        g.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        //Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);

        //Urm Buttons
        menuButton.draw(g);
        replayButton.draw(g);
        unpauseButton.draw(g);

    }

    public void update() {
        musicButton.update();
        sfxButton.update();

        menuButton.update();
        replayButton.update();
        unpauseButton.update();
    }

    private boolean isIn(MouseEvent e, PauseButton button) {
        return button.getBounds().contains(e.getX(), e.getY());
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if(isIn(e, musicButton))
            musicButton.setMousePressed(true);
        else if(isIn(e, sfxButton))
            sfxButton.setMousePressed(true);
        else if(isIn(e, unpauseButton))
            unpauseButton.setMousePressed(true);
        else if(isIn(e, menuButton))
            menuButton.setMousePressed(true);
        else if(isIn(e, replayButton))
            replayButton.setMousePressed(true);
    }

    public void mouseReleased(MouseEvent e) {
        if(isIn(e, musicButton) && musicButton.isMousePressed())
            musicButton.setMuted(musicButton.isMuted());
        if(isIn(e, sfxButton) && sfxButton.isMousePressed())
            sfxButton.setMuted(sfxButton.isMuted());
        else if(isIn(e, menuButton) && menuButton.isMousePressed()) {
            GameState.state = GameState.MENU;
            playing.unpauseGame();
        } else if(isIn(e, replayButton) && replayButton.isMousePressed())
            System.out.println("Replay level");
        else if(isIn(e, unpauseButton) && unpauseButton.isMousePressed())
            playing.unpauseGame();

        musicButton.resetBools();
        sfxButton.resetBools();
        unpauseButton.resetBooleans();
        replayButton.resetBooleans();
        replayButton.resetBooleans();
    }

    public void mouseMoved(MouseEvent e) {
        musicButton.setMouseOver(false);
        sfxButton.setMouseOver(false);
        unpauseButton.setMouseOver(false);
        replayButton.setMouseOver(false);
        menuButton.setMouseOver(false);

        if(isIn(e, musicButton))
            musicButton.setMouseOver(true);
        else if(isIn(e, sfxButton))
            sfxButton.setMouseOver(true);
        else if(isIn(e, unpauseButton))
            unpauseButton.setMouseOver(true);
        else if(isIn(e, menuButton))
            menuButton.setMouseOver(true);
        else if(isIn(e, replayButton))
            replayButton.setMouseOver(true);
    }
}

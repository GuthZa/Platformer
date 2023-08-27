package ui;

import game.Game;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static utilz.Constants.UI.PauseButtons.*;

public class PauseOverlay {

    private BufferedImage backgroundImage;
    private SoundButton musicButton, sfxButton;

    private int backgroundX, backgroundY, backgroundWidth, backgroundHeight;
    public PauseOverlay() {
        loadBackground();
        createSoundButtons();
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

    public void draw(Graphics g) {
        //Background
        g.drawImage(backgroundImage, backgroundX, backgroundY, backgroundWidth, backgroundHeight, null);

        //Sound Buttons
        musicButton.draw(g);
        sfxButton.draw(g);
    }

    public void update() {

    }
    public void mouseDragged() {

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }
}

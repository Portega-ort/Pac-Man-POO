package lospedros.edu.pacman.audio;

public interface SoundPlayer {
    void playIntro();
    void playChomp();
    void playBonus();
    void playDeath();
    void startSirenLoop();
    void stopSirenLoop();
}
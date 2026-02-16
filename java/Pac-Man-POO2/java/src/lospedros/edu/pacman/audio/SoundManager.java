package lospedros.edu.pacman.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class SoundManager implements SoundPlayer {
    private static final float SAMPLE_RATE = 44100f;
    private final boolean audioHabilitado;
    private Clip sirenClip;

    public SoundManager() {
        this(true);
    }

    public SoundManager(boolean audioHabilitado) {
        this.audioHabilitado = audioHabilitado;
    }

    public boolean isAudioHabilitado() {
        return audioHabilitado;
    }

    @Override
    public void playIntro() {
        if (!audioHabilitado) {
            return;
        }
        new Thread(() -> {
            playToneBlocking(440, 180, 0.6);
            sleep(60);
            playToneBlocking(660, 180, 0.6);
            sleep(60);
            playToneBlocking(880, 200, 0.6);
        }, "Pacman-Intro-Sound").start();
    }

    @Override
    public void playChomp() {
        if (!audioHabilitado) {
            return;
        }
        playTone(820, 70, 0.5);
    }

    @Override
    public void playBonus() {
        if (!audioHabilitado) {
            return;
        }
        playTone(1200, 150, 0.7);
    }

    @Override
    public void playDeath() {
        if (!audioHabilitado) {
            return;
        }
        playTone(240, 500, 0.8);
    }

    @Override
    public void startSirenLoop() {
        if (!audioHabilitado) {
            return;
        }
        if (sirenClip != null && sirenClip.isRunning()) {
            return;
        }
        sirenClip = createToneClip(300, 600, 0.05);
        sirenClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    @Override
    public void stopSirenLoop() {
        if (sirenClip != null) {
            sirenClip.stop();
            sirenClip.close();
            sirenClip = null;
        }
    }

    private void playTone(int frequency, int durationMs, double volume) {
        new Thread(() -> {
            Clip clip = createToneClip(frequency, durationMs, volume);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
            clip.start();
        }, "Pacman-Tone-" + frequency).start();
    }

    private void playToneBlocking(int frequency, int durationMs, double volume) {
        Clip clip = createToneClip(frequency, durationMs, volume);
        clip.start();
        sleep(durationMs + 20);
        clip.close();
    }

    private Clip createToneClip(int frequency, int durationMs, double volume) {
        int numSamples = (int) (durationMs * SAMPLE_RATE / 1000.0);
        byte[] data = new byte[numSamples * 2];
        double twoPiF = 2.0 * Math.PI * frequency;

        for (int i = 0; i < numSamples; i++) {
            double time = i / SAMPLE_RATE;
            short sample = (short) (Math.sin(twoPiF * time) * 32767 * volume);
            data[i * 2] = (byte) (sample & 0xff);
            data[i * 2 + 1] = (byte) ((sample >> 8) & 0xff);
        }

        AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(format, data, 0, data.length);
            return clip;
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo cargar el audio", e);
        }
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
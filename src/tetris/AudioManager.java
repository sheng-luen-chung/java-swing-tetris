package tetris;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioManager {
    private static final String BACKGROUND_MUSIC_PATH = "assets/audio/katusha.mid";
    private static final String LINE_CLEAR_SOUND_PATH = "assets/audio/clear.wav";
    private static final String DROP_SOUND_PATH = "assets/audio/drop.wav";
    private static final float BASE_TEMPO_FACTOR = 1.0f;
    private static final float TEMPO_STEP_PER_LEVEL = 0.08f;
    private static final float MAX_TEMPO_FACTOR = 2.0f;

    private Sequencer sequencer;
    private boolean musicAvailable;
    private boolean musicEnabled;
    private boolean missingMusicWarningShown;
    private float tempoFactor = BASE_TEMPO_FACTOR;

    public AudioManager() {
        musicAvailable = new File(BACKGROUND_MUSIC_PATH).isFile();
        if (!musicAvailable) {
            warnMissingMusic();
        }
    }

    public void playBackgroundMusic() {
        if (!isMusicAvailable()) {
            musicEnabled = false;
            warnMissingMusic();
            return;
        }

        try {
            if (sequencer == null || !sequencer.isOpen()) {
                Sequence sequence = MidiSystem.getSequence(new File(BACKGROUND_MUSIC_PATH));
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
                sequencer.setSequence(sequence);
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            }

            sequencer.setTempoFactor(tempoFactor);
            sequencer.start();
            musicEnabled = true;
        } catch (InvalidMidiDataException | IOException | MidiUnavailableException ex) {
            musicAvailable = false;
            musicEnabled = false;
            System.out.println("Warning: could not play MIDI background music: " + ex.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        if (sequencer != null && sequencer.isOpen()) {
            sequencer.stop();
        }
        musicEnabled = false;
    }

    public void toggleMusic() {
        if (musicEnabled) {
            stopBackgroundMusic();
        } else {
            playBackgroundMusic();
        }
    }

    public void setMusicLevel(int level) {
        int safeLevel = Math.max(1, level);
        tempoFactor = Math.min(MAX_TEMPO_FACTOR, BASE_TEMPO_FACTOR + (safeLevel - 1) * TEMPO_STEP_PER_LEVEL);

        if (sequencer != null && sequencer.isOpen()) {
            sequencer.setTempoFactor(tempoFactor);
        }
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isMusicAvailable() {
        musicAvailable = new File(BACKGROUND_MUSIC_PATH).isFile();
        return musicAvailable;
    }

    public void playLineClearSound() {
        playSoundEffect(LINE_CLEAR_SOUND_PATH);
    }

    public void playDropSound() {
        playSoundEffect(DROP_SOUND_PATH);
    }

    private void playSoundEffect(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return;
        }

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    event.getLine().close();
                    try {
                        audioInputStream.close();
                    } catch (IOException ex) {
                        System.out.println("Warning: could not close audio stream: " + ex.getMessage());
                    }
                }
            });
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Warning: could not play sound effect " + path + ": " + ex.getMessage());
        }
    }

    private void warnMissingMusic() {
        if (!missingMusicWarningShown) {
            System.out.println("Warning: MIDI background music not found at " + BACKGROUND_MUSIC_PATH);
            missingMusicWarningShown = true;
        }
    }
}

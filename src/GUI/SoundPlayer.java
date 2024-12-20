package GUI;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class SoundPlayer {
    public static void playSound(String soundFileName) {
        soundFileName = "/sound/" + soundFileName;
        try (InputStream inputStream = SoundPlayer.class.getResourceAsStream(soundFileName)) {
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static Clip playMusic(String musicFileName) {
        musicFileName = "/sound/music/" + musicFileName;

        try (InputStream inputStream = SoundPlayer.class.getResourceAsStream(musicFileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Not found: " + musicFileName);
            }
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
            return clip;
        } catch (Exception e) {
            System.out.println("Audio error.");
        }
        return null;
    }

    public static void playRandomSound(String DirName) {
        // only support 6 pieces of music
        Random random = new Random();
        int num = random.nextInt(3, 7);
        String musicFileName = String.format(DirName + "Positive_00%s.wav", num);
//        System.out.println(musicFileName);
        try (InputStream inputStream = SoundPlayer.class.getResourceAsStream(musicFileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Not found: " + musicFileName);
            }
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Audio error.");
        }
    }

    public static void playRandomMusic(String DirName) {
        // only support 6 pieces of music
        Random random = new Random();
        int num = random.nextInt(1, 3);
        String musicFileName = String.format(DirName + "theme_%d.wav", num);
        try (InputStream inputStream = SoundPlayer.class.getResourceAsStream(musicFileName)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Not found: " + musicFileName);
            }
            BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Audio error.");
        }
    }
}

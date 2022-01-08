package com.davidalmarinho.game_engine.utils;

import com.davidalmarinho.game_engine.engine_core.GameEngine;

import java.io.*;
import java.util.Objects;
import javax.sound.sampled.*;

public class Sound {
    public static class Clips {
        public Clip[] clips;
        private int p;
        private int count;

        public Clips(byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
            if (buffer == null)
                return;

            clips = new Clip[count];
            this.count = count;

            for (int i = 0; i < count; i++) {
                clips[i] = AudioSystem.getClip();
                clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
            }
        }

        public void play() {
            if (clips == null) return;
            clips[p].stop();
            clips[p].setFramePosition(0);
            clips[p].start();
            p++;
            if (p >= count) p = 0;
        }

		/*public void loop() {
			if(clips == null) return;
			clips[p].loop(300);
		}*/

        public void loopLessLoudSongs() {
            if (clips == null) return;
            FloatControl gainControl = (FloatControl) clips[p].getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);
            clips[p].loop(300);
        }
    }

    public static Clips gameMusic = load("/Heavy-synth-loop-126-bpm.wav");
    public static Clips pickBall = load("/pickBall.wav");
    public static Clips enterPortal = load("/portal.wav");
    public static Clips hurt = load("/hurtPlayer.wav");
    public static Clips pickSuperBall = load("/pickSuperBall.wav");

    private static Clips load(String name) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataInputStream dis = new DataInputStream(Objects.requireNonNull(Sound.class.getResourceAsStream(name)));

            byte[] buffer = new byte[1024];
            int read;
            while ((read = dis.read(buffer)) >= 0) {
                baos.write(buffer, 0, read);
            }
            dis.close();
            byte[] data = baos.toByteArray();
            return new Clips(data, 1);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                GameEngine.errorOccurred = true;
                return new Clips(null, 0);
            } catch (Exception ee) {
                return null;
            }
        }
    }
}

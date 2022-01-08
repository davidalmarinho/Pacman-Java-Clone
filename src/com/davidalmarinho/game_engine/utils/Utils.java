package com.davidalmarinho.game_engine.utils;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.main.Game;
import com.davidalmarinho.main.entities.Entity;
import com.davidalmarinho.main.entities.PacMan;

import java.io.*;

public class Utils {
    public static boolean isWindows() {
        return System.getProperty("os.name").contains("Windows");
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").contains("Linux");
    }

    public static boolean isMacOS() {
        return System.getProperty("os.name").contains("Mac");
    }

    public static void saveGame(String[] opt, int[] value, int encode) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("save.txt"));
        } catch (IOException e) {
            e.printStackTrace();
            GameEngine.errorOccurred = true;
        }
        for (int i = 0; i < opt.length; i++) {
            StringBuilder saveCurrent = new StringBuilder(opt[i]);
            saveCurrent.append(":");
            char[] curCharToConvert = String.valueOf(value[i]).toCharArray();

            for (int j = 0; j < curCharToConvert.length; j++) {
                curCharToConvert[j] += encode;
                saveCurrent.append(curCharToConvert[j]);
            }
            try {
                assert null != writer;
                writer.write(String.valueOf(saveCurrent));
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
                GameEngine.errorOccurred = true;
            }
        }
        try {
            assert null != writer;
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            GameEngine.errorOccurred = true;
        }
    }

    public static String loadGame(int encode) {
        File file = new File("save.txt");
        StringBuilder current = new StringBuilder();

        if (file.exists()) {
            String analyser;
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader("save.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                GameEngine.errorOccurred = true;
            }

            try {
                while (true) {
                    assert reader != null;
                    if ((analyser = reader.readLine()) == null) break;
                    String[] navigator = analyser.split(":");
                    char[] decode = navigator[1].toCharArray();
                    navigator[1] = "";
                    for (int i = 0; i < decode.length; i++) {
                        decode[i] -= encode;
                        navigator[1] += decode[i];
                    }

                    current.append(navigator[0]);
                    current.append(":");
                    current.append(navigator[1]);
                    current.append("/");
                }
            } catch (IOException ioE) {
                ioE.printStackTrace();
                GameEngine.errorOccurred = true;
            }
        }
        return String.valueOf(current);
    }

    public static void applySaver(String line) {
        String[] fullLine = line.split("/");
        for (int i = 0; i < fullLine.length; i++) {
            String[] navigator = fullLine[i].split(":");
            // [0] -> Item type
            // [1] -> Item value
            Entity entity;
            for (int j = 0; j < Game.entities.size(); j++) {
                entity = Game.entities.get(j);
                if (entity instanceof PacMan) {
                    if ("high_score".equals(navigator[0])) {
                        ((PacMan) entity).setHighScore(Integer.parseInt(navigator[1]));
                        break;
                    }
                }
            }
        }
    }
}

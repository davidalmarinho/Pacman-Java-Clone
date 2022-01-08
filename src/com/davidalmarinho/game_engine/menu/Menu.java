package com.davidalmarinho.game_engine.menu;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.utils.Utils;
import com.davidalmarinho.main.Game;
import com.davidalmarinho.main.entities.Entity;
import com.davidalmarinho.main.entities.PacMan;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Menu {
    private final String[] options = {"play", "exit"};
    private int curOpt = 0;
    private final int maxOpt = options.length - 1;
    private Font verdana;
    private Font verdanaHighScore;

    public Menu() {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFJVerdana12pt.ttf");
        InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFJVerdana12pt.ttf");
        try {
            assert stream != null;
            verdana = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(14f);
            assert stream2 != null;
            verdanaHighScore = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(13f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            GameEngine.errorOccurred = true;
        }
    }

    public void tick(GameEngine gameEngine) {
        if (!gameEngine.getGameState().equals("menu")) {
            return;
        }

        if (gameEngine.getInput().isKeyDown(KeyEvent.VK_UP)) {
            curOpt++;
            if (curOpt > maxOpt) {
                curOpt = 0;
            }
        }

        if (gameEngine.getInput().isKeyDown(KeyEvent.VK_DOWN)) {
            curOpt--;
            if (curOpt < 0) {
                curOpt = maxOpt;
            }
        }

        boolean enter = gameEngine.getInput().isKeyDown(KeyEvent.VK_ENTER);
        if (!enter) {
            return;
        }

        if (options[curOpt].equals("play")) {
            gameEngine.setGameState("normal");
        } else if (options[curOpt].equals("exit")) {
            System.exit(1);
        }
        this.loadHighScore();
    }

    public void render(Graphics g, GameEngine gameEngine) {
        if (!gameEngine.getGameState().equals("menu")) {
            return;
        }
        int WIDTH = gameEngine.getRenderer().getWIDTH();
        int HEIGHT = gameEngine.getRenderer().getHEIGHT();
        int SCALE = gameEngine.getRenderer().getSCALE();
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(new Color(0, 0, 0, 240));
        g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

        g.setColor(new Color(255, 255, 255));
        g.setFont(verdana);
        // Put positions
        int xOff = 16;
        int yOff = 24;
        int xPlay = (WIDTH / 2) * SCALE - xOff;
        int yPlay = (HEIGHT / 2) * SCALE - yOff;
        g.drawString("Play", xPlay, yPlay);
        int xExit = (WIDTH / 2) * SCALE - xOff;
        int yExit = (HEIGHT / 2) * SCALE + yOff;
        g.drawString("Exit", xExit, yExit);

        switch (options[curOpt]) {
            case "play":
                g.setColor(new Color(new Random().nextInt(256),
                        new Random().nextInt(256), new Random().nextInt(256)));
                g.drawString("Play", xPlay, yPlay);
                int minusX = 32;
                g.drawString("> ", xPlay - minusX, yPlay);
            break;
            case "exit":
                g.setColor(new Color(new Random().nextInt(256),
                        new Random().nextInt(256), new Random().nextInt(256)));
                g.drawString("Exit", xExit, yExit);
                int minusXExit = 32;
                g.drawString("> ", xExit - minusXExit, yExit);
            break;
        }
        this.showHighScore(gameEngine, g);
    }

    private void loadHighScore() {
        File file = new File("save.txt");
        if (file.exists()) {
            String saver = Utils.loadGame(20);
            Utils.applySaver(saver);
        }
    }

    private void showHighScore(GameEngine gameEngine, Graphics g) {
        int WIDTH = gameEngine.getRenderer().getWIDTH();
        int HEIGHT = gameEngine.getRenderer().getHEIGHT();
        int SCALE = gameEngine.getRenderer().getSCALE();
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity entity = Game.entities.get(i);
            if (entity instanceof PacMan) {
                loadHighScore();
                if (((PacMan) entity).getHighScore() != 0) {
                    g.setFont(verdanaHighScore);
                    g.setColor(new Color(255, 255, 255));
                    g.drawString("High Score: " + ((PacMan) entity).getHighScore(), (WIDTH / 2) * SCALE - 80,
                            HEIGHT * SCALE - 64);
                }
                break;
            }
        }
    }
}

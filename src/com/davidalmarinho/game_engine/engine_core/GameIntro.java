package com.davidalmarinho.game_engine.engine_core;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameIntro {
    private int mainFps = 0;
    private int stringFps = 254;
    private int disappearStringFps = 1;

    public void tick(GameEngine gameEngine) {
        if (gameEngine.getGameState().equals("game_introduction")) {
            mainFps ++;

            if (mainFps <= 40) {
                return;
            }

            if (stringFps > 0) {
                stringFps -= 2;
            } else if (stringFps == 0) {
                if (disappearStringFps == 255) {
                    gameEngine.setGameState("menu");
                }
                if (disappearStringFps != 255) {
                    disappearStringFps += 2;
                }
            }

            if (gameEngine.getInput().isKeyUp(KeyEvent.VK_ENTER)) {
                gameEngine.setGameState("menu");
            }
        }
    }

    public void render(GameEngine gameEngine, Graphics g) {
        int WIDTH = gameEngine.getRenderer().getWIDTH();
        int HEIGHT = gameEngine.getRenderer().getHEIGHT();
        int SCALE = gameEngine.getRenderer().getSCALE();
        if (gameEngine.getGameState().equals("game_introduction")) {
            mainFps ++;
            g.setColor(new Color(0, 0, 0));
            g.fillRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);

            if (mainFps <= 40) {
                return;
            }

            Graphics2D g2D = (Graphics2D) g;
            g2D.setFont(new Font("arial", Font.BOLD, 40));
            g2D.setColor(new Color(255, 255, 255));
            g2D.drawString("By davidalmarinho", (WIDTH * SCALE) / 2 - 176,
                    (HEIGHT * SCALE) / 2);
            if (stringFps > 0) {
                g2D.setColor(new Color(0, 0, 0, stringFps));
            } else if (stringFps == 0) {
                g2D.setColor(new Color(0, 0, 0, disappearStringFps));
            }
            g2D.fillRect((WIDTH * SCALE) / 2 - 251,
                    (HEIGHT * SCALE) / 2 - 44, 530, 65);
        }
    }
}

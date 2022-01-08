package com.davidalmarinho.game_engine.engine_core;


import com.davidalmarinho.game_engine.map.Map;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;

public class GameOver {
    private int mainFps = 0;
    private int light = 0;
    private boolean renderGameOverScreen = false;
    private Font verdanaFont;
    private int sparkleFps = 0;

    public GameOver() {
        this.loadDefaultFont();
    }

    private void loadDefaultFont() {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFJVerdana12pt.ttf");
        try {
            assert stream != null;
            verdanaFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(20f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            GameEngine.errorOccurred = true;
        }
    }

    public void tick(GameEngine gameEngine) {
        this.defaultGameOverMechanism(gameEngine);
    }

    private void defaultGameOverMechanism(GameEngine gameEngine) {
        // Background animation system
        if (!(gameEngine.getGameState().equals("game_over"))) {
            return;
        }
        if (light >= 0 && !renderGameOverScreen) {
            light++;
            if (light == 255) {
                renderGameOverScreen = true;
            }
        }

        // GameOver words animation
        if (renderGameOverScreen) {
            sparkleFps ++;
            if (sparkleFps == 140) {
                sparkleFps = 0;
            }
        }
        this.setGameState(gameEngine);
    }

    private void setGameState(GameEngine gameEngine) {
        mainFps ++;
        if (mainFps >= 600 || gameEngine.getInput().isKeyUp(KeyEvent.VK_ENTER)) {
            Map.restartGame(gameEngine, 1);
            gameEngine.setGameState("menu");
        }
    }

    public void render(GameEngine gameEngine, Graphics g) {
        this.renderDefaultGameOverInterface(g, gameEngine);
    }

    private void renderDefaultGameOverInterface(Graphics g, GameEngine gameEngine) {
        if (!(gameEngine.getGameState().equals("game_over"))) {
            return;
        }

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(new Color(0, 0, 0, light));
        g2D.fillRect(0, 0, gameEngine.getRenderer().getWIDTH() * gameEngine.getRenderer().getHEIGHT(),
                gameEngine.getRenderer().getHEIGHT() * gameEngine.getRenderer().getSCALE());

        if (renderGameOverScreen) {
            g2D.setFont(verdanaFont);
            g2D.setColor(new Color(255, 0, 0));
            if (sparkleFps > 90) {
                return;
            }
            int breaker = 96;
            g2D.drawString("Game Over",
                    (gameEngine.getRenderer().getWIDTH() * gameEngine.getRenderer().getSCALE() / 2) - breaker,
                    (gameEngine.getRenderer().getWIDTH() * gameEngine.getRenderer().getSCALE() / 2) - breaker);
        }
    }
}

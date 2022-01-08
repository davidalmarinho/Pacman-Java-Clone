package com.davidalmarinho.game_engine.menu;

import com.davidalmarinho.game_engine.engine_core.GameEngine;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class PauseMenu {
    private boolean pauseMenu = false;
    private int sparkleFps = 0;
    private boolean mayRender = false;
    private Font verdana;

    public PauseMenu() {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFJVerdana12pt.ttf");

        try {
            assert stream != null;
            verdana = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(14f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            GameEngine.errorOccurred = true;
        }
    }

    public void tick(GameEngine gameEngine) {
        if (pauseMenu) {
            gameEngine.setGameState("pause_menu");
        } else if (gameEngine.getGameState().equals("pause_menu")) {
            gameEngine.setGameState("normal");
        }

        if (!gameEngine.getGameState().equals("pause_menu")) {
            return;
        }

        sparkleFps++;
        if (sparkleFps < 120) {
            mayRender = true;
        } else if (sparkleFps < 180) {
            mayRender = false;
        } else {
            sparkleFps = 0;
        }
    }

    public void render(GameEngine gameEngine, Graphics g) {
        if (!gameEngine.getGameState().equals("pause_menu")) {
            return;
        }
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(new Color(0, 0, 0, 240));
        g2D.fillRect(0, 0,
                gameEngine.getRenderer().getWIDTH() * gameEngine.getRenderer().getSCALE(),
                gameEngine.getRenderer().getHEIGHT() * gameEngine.getRenderer().getWIDTH());

        if (mayRender) {
            g2D.setFont(verdana);
            g2D.setColor(new Color(new Random().nextInt(256),
                    new Random().nextInt(256),
                    new Random().nextInt(256)));
            g2D.drawString("> Press Enter <",
                    (gameEngine.getRenderer().getWIDTH() * gameEngine.getRenderer().getSCALE()) / 2 - (gameEngine.getRenderer().getWIDTH() / 3),
                    (gameEngine.getRenderer().getHEIGHT() * gameEngine.getRenderer().getSCALE()) / 2);
        }
    }

    public boolean isPauseMenu() {
        return pauseMenu;
    }

    public void setPauseMenu(boolean pauseMenu) {
        this.pauseMenu = pauseMenu;
    }
}

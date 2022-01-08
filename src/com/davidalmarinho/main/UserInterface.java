package com.davidalmarinho.main;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.main.entities.Entity;
import com.davidalmarinho.main.entities.PacMan;
import com.davidalmarinho.main.entities.Phantom;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class UserInterface {
    InputStream stream = ClassLoader.getSystemClassLoader()
            .getResourceAsStream("PixelFJVerdana12pt.ttf");
    private Font verdanaFont;

    public UserInterface() {
        // Load Font
        try {
            assert stream != null;
            verdanaFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(8f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            GameEngine.errorOccurred = true;
        }
    }

    public void render(GameEngine gameEngine, Graphics g) {
        g.setColor(new Color(255, 255, 0));
        g.setFont(verdanaFont);
        g.drawString("Points -> " + PacMan.points, 16, 32);
        g.setColor(new Color(0, 255, 0));
        g.drawString("Lives -> " + PacMan.lives, 16, 48);
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Phantom) {
                ((Phantom) e).renderSleep(gameEngine, g);
            }
        }
    }
}

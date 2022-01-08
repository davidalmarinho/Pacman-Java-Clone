package com.davidalmarinho.game_engine.map.tiles;

import com.davidalmarinho.game_engine.engine_core.GameEngine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Spritesheet {
    private BufferedImage tileSpritesheet;

    public Spritesheet() {
        try {
            tileSpritesheet = ImageIO.read(getClass().getResource("/Spritesheet.png"));
        } catch (IOException ioE) {
            ioE.printStackTrace();
            GameEngine.errorOccurred = true;
        }
    }

    public BufferedImage getSprite(int x, int y, int width, int height) {
        return tileSpritesheet.getSubimage(x, y, width, height);
    }
}

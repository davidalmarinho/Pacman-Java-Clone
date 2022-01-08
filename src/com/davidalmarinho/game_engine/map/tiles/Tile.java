package com.davidalmarinho.game_engine.map.tiles;

import com.davidalmarinho.game_engine.map.Camera;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private final int x, y;
    private final BufferedImage tile;

    public Tile(int x, int y, BufferedImage tile) {
        this.x = x;
        this.y = y;
        this.tile = tile;
    }

    public void render(Graphics g) {
        g.drawImage(tile, this.x - Camera.x, this.y - Camera.y, null);
    }
}

package com.davidalmarinho.game_engine.map.tiles;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.map.Map;

import java.awt.image.BufferedImage;

public class WallTile extends Tile {
    public static BufferedImage wallTile =
            GameEngine.spritesheet.getSprite(0, Map.tileSize, Map.tileSize, Map.tileSize);
    public WallTile(int x, int y, BufferedImage tile) {
        super(x, y, tile);
    }
}

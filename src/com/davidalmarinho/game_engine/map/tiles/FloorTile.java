package com.davidalmarinho.game_engine.map.tiles;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.map.Map;

import java.awt.image.BufferedImage;

public class FloorTile extends Tile {
    public static BufferedImage floorTile =
            GameEngine.spritesheet.getSprite(0, 0, Map.tileSize, Map.tileSize);
    public FloorTile(int x, int y, BufferedImage tile) {
        super(x, y, tile);
    }
}

package com.davidalmarinho.game_engine.map;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.map.tiles.FloorTile;
import com.davidalmarinho.game_engine.map.tiles.Tile;
import com.davidalmarinho.game_engine.map.tiles.WallTile;
import com.davidalmarinho.main.Game;
import com.davidalmarinho.main.entities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
    private BufferedImage map;
    public static Tile[] tiles;
    public static int tileSize = 16;
    public static int mapWidth;
    public static int mapHeight;
    private int exponent = 4;

    public Map(int curLevel) {
        if (tileSize == 32) {
            exponent = 5;
        } else if (tileSize == 64) {
            exponent = 6;
        } else if (tileSize == 124) {
            exponent = 7;
        } else if (tileSize == 258) {
            exponent = 8;
        }

        try {
            map = ImageIO.read(getClass().getResource("/map" + curLevel + ".png"));
            int width = map.getWidth();
            int height = map.getHeight();
            mapWidth = width;
            mapHeight = height;
            tiles = new Tile[width * height];
            int[] pixels = new int[width * height];
            map.getRGB(0, 0, width, height, pixels, 0, width);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int currentPixel = pixels[x + (y * width)];
                    tiles[x + (y * width)] = new FloorTile(x * tileSize, y * tileSize, FloorTile.floorTile);

                    if (currentPixel == 0xFF000000) {
                        tiles[x + (y * width)] = new FloorTile(x * tileSize, y * tileSize, FloorTile.floorTile);
                    } else if (currentPixel == 0xFFffffff) {
                        tiles[x + (y * width)] = new WallTile(x * tileSize, y * tileSize, WallTile.wallTile);

                    } else if (currentPixel == 0xFF0000ff) {
                        PacMan pacMan = new PacMan(x * tileSize, y * tileSize, tileSize, tileSize);
                        Game.entities.add(pacMan);
                    } else if (currentPixel == 0xFFffff00) {
                        Ball ball = new Ball(x * tileSize, y * tileSize, 4, 4);
                        Game.entities.add(ball);
                    }else if (currentPixel == 0xFF969696) {
                        SuperBall superBall = new SuperBall(x * tileSize, y * tileSize, 6, 6);
                        Game.entities.add(superBall);

                    } else if (currentPixel == 0xFFff0000) {
                        Phantom phantom = new Phantom(x * tileSize, y * tileSize, tileSize, tileSize);
                        Game.entities.add(phantom);

                    } else if (currentPixel == 0xFF00855b) {
                        Portal leftPortal = new Portal(x * tileSize, y * tileSize,
                                1, 16, "left");
                        Game.entities.add(leftPortal);
                    } else if (currentPixel == 0xFF00845b) {
                        Portal rightPortal = new Portal(x * tileSize, y * tileSize,
                                1, 16, "right");
                        Game.entities.add(rightPortal);
                    }
                }
            }

        } catch (IOException ioE) {
            GameEngine.errorOccurred = true;
        }
    }

    public static boolean isFree(int xNext, int yNext, int speed, int width, int height) {
        int xStart = xNext / tileSize;
        int yStart = yNext / tileSize;

        int xFinal = (xNext + width - speed) / tileSize;
        int yFinal = (yNext + height - speed) / tileSize;

        for (int x = xStart; x <= xFinal; x++) {
            for (int y = yStart; y <= yFinal; y++) {
                if (tiles[x + (y * mapWidth)] instanceof WallTile) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void restartGame(GameEngine gameEngine, int curLevel) {
        Game.entities.clear();
        Game.entities = new ArrayList<>();
        gameEngine.setMap(new Map(curLevel));
    }

    public void render(Graphics g, GameEngine gameEngine) {
        int xStart = Camera.x >> exponent;
        int yStart = Camera.y >> exponent;

        int xFinal = xStart + (gameEngine.getRenderer().getWIDTH() >> 4);
        int yFinal = yStart + (gameEngine.getRenderer().getHEIGHT() >> 4);

        for (int x = xStart; x <= xFinal; x++) {
            for (int y = yStart; y <= yFinal; y++) {
                if (x < 0 || x >= map.getWidth() || y < 0 || y >= map.getHeight()) {
                    continue;
                }
                Tile tile = tiles[x + (y * map.getWidth())];
                tile.render(g);
            }
        }
    }
}

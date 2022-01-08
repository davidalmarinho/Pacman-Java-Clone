package com.davidalmarinho.main.entities;

import com.davidalmarinho.game_engine.a_star.Node;
import com.davidalmarinho.game_engine.a_star.Vector2i;
import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.map.Camera;
import com.davidalmarinho.game_engine.map.Map;

import java.awt.*;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public abstract class Entity {
    private int x, y;
    private final int width, height;
    protected int xMask, yMask, widthMask, heightMask;

    protected int speed;
    // protected int index;
    // protected int fps = 0;

    // A*
    protected List<Node> path;

    // Depth
    protected int depth;

    public Entity(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /*protected void animationsSystem(int maxFps, int maxIndex) {
        fps++;
        if (fps > maxFps) {
            fps = 0;
            index++;
        }

        if (index == maxIndex) {
            index = 0;
        }
    }*/

    protected boolean isColliding(Entity e1, Entity e2) {
        Rectangle e1R = new Rectangle(e1.getX() + e1.xMask, e1.getY() + e1.yMask,
                e1.widthMask, e1.heightMask);
        Rectangle e2R = new Rectangle(e2.getX() + e2.xMask, e2.getY() + e2.yMask,
                e2.widthMask, e2.heightMask);
        return e1R.intersects(e2R);
    }

    protected void setMask(int xMask, int yMask, int widthMask, int heightMask) {
        this.xMask = xMask;
        this.yMask = yMask;
        this.widthMask = widthMask;
        this.heightMask = heightMask;
    }

    protected void seeMask(Graphics g) {
        g.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255)));
        g.fillRect(getX() - Camera.x + xMask, getY() - Camera.y + yMask, widthMask, heightMask);
    }

    protected void updateCamera(GameEngine gameEngine) {
        Camera.x = Camera.clamp(this.getX() - (gameEngine.getRenderer().getWIDTH() / 2), 0,
                Map.mapWidth * Map.tileSize - gameEngine.getRenderer().getWIDTH());
        Camera.y = Camera.clamp(this.getY() - (gameEngine.getRenderer().getHEIGHT() / 2), 0,
                Map.mapHeight * Map.tileSize - gameEngine.getRenderer().getHEIGHT());
    }

    public void followPath4Dirs(List<Node> path, int speed, Phantom phantom) {
        if (path != null && path.size() > 0) {
            Vector2i target = path.get(path.size() - 1).originOfThePath;
            int size = Map.tileSize;

            // This that will force the enemy to circumvent the wall
            int dir = 0;
            if (x != target.x * size) {
                if (x < target.x * size) {
                    if (!Map.isFree((x + speed), getY(), speed, getWidth(), getHeight())) {
                        dir = 1;
                    }
                } else if (x > (target.x * size)) {
                    if (!Map.isFree((x - speed), getY(), speed, getWidth(), getHeight())) {
                        dir = 1;
                    }
                }
            } else {
                dir = 1;
            }

            if (dir == 0) {
                if (x < target.x * size) {
                    x += speed;
                    phantom.setCurDir(phantom.getRightDir());
                    if (x > target.x * size) {
                        x = target.x * size;
                    }
                } else if (x > (target.x * size)) {
                    x -= speed;
                    phantom.setCurDir(phantom.getLeftDir());
                    if (x < target.x * size) {
                        x = target.x * size;
                    }
                }
            } else {
                if (y < (target.y * size)) {
                    y += speed;
                    phantom.setCurDir(phantom.getDownDir());
                    if (y > target.y * size) {
                        y = target.y * size;
                    }
                } else if (y > (target.y * size)) {
                    y -= speed;
                    phantom.setCurDir(phantom.getUpDir());
                    if (y < target.y * size) {
                        y = target.y * size;
                    }
                }
            }

            if ((x == target.x * size) && (y == target.y * size)) {
                path.remove(path.size() - 1);
            }
        }
    }

    // Depth
    public static Comparator<Entity> entitySorter = Comparator.comparingInt(object -> object.depth);

    public void tick(GameEngine gameEngine) {

    }

    public void render(Graphics g) {

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}

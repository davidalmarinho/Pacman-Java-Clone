package com.davidalmarinho.main.entities;

import com.davidalmarinho.game_engine.a_star.AStar;
import com.davidalmarinho.game_engine.a_star.Vector2i;
import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.map.Camera;
import com.davidalmarinho.game_engine.map.Map;
import com.davidalmarinho.main.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class Phantom extends Entity {
    // Beginning
    private int startMoveFps = 0;
    private final int startMoveTime = new Random().nextInt(7 * 60) + 5 * 60;
    // Ghost mode
    private boolean ghostMode = false;
    private int ghostModeFps = 0;
    int bound = 60 * 3;
    int minTime = 60 * 2;
    private int randomTimeForGhostMode = new Random().nextInt(bound) + minTime;
    // Movement
    private final int rightDir = 2;
    private final int leftDir = 1;
    private final int downDir = 3;
    private final int upDir = 4;
    private int curDir = rightDir;
    private boolean damaged;
    private boolean erasePath = true;
    private int feedBackFps = 0;
    private boolean feedBack = false;
    // Animations
    private BufferedImage[] redPhantomAnimations;
    private BufferedImage[] damagedPhantomAnimations;
    private boolean sleeping = true;
    private int zFps = 0;
    private int xZPos, yZPos;
    private Font downLetterZVerdana;
    private Font middleLetterZVerdana;
    private Font topLetterVerdana;
    private Font curF;
    private int sparkleFeedBackFps = 0;
    private boolean mayRender;

    public Phantom(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.speed = 1;
        depth = 1;
        setMask(4, 4, 8, 9);
        this.loadSprites();
        this.insertFonts();
    }

    private void insertFonts() {
        InputStream stream1 = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFJVerdana12pt.ttf");
        InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFJVerdana12pt.ttf");
        InputStream stream3 = ClassLoader.getSystemClassLoader().getResourceAsStream("PixelFJVerdana12pt.ttf");
        try {
            assert stream1 != null;
            downLetterZVerdana = Font.createFont(Font.TRUETYPE_FONT, stream1).deriveFont(4f);
            assert stream2 != null;
            middleLetterZVerdana = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(5f);
            assert stream3 != null;
            topLetterVerdana = Font.createFont(Font.TRUETYPE_FONT, stream3).deriveFont(6f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            GameEngine.errorOccurred = true;
        }
    }

    private void loadSprites() {
        redPhantomAnimations = new BufferedImage[5];
        for (int i = 0; i < redPhantomAnimations.length; i++) {
            redPhantomAnimations[i] = GameEngine.spritesheet.getSprite(i * 16, 64, 16, 16);
        }
        damagedPhantomAnimations = new BufferedImage[4];
        for (int i = 0; i < damagedPhantomAnimations.length; i++) {
            damagedPhantomAnimations[i] = GameEngine.spritesheet.getSprite(i * 16, 80, 16, 16);
        }
    }

    public void tick(GameEngine gameEngine) {
        this.sleepingMechanism();

        // Only when they wake up
        if (startMoveFps >= startMoveTime) {
            sleeping = false;
            // Follow Player
            if (!damaged) {
                erasePath = true;
                this.ghostModeSystem();
                if (ghostMode) {
                    this.enemiesFindRandomPath();
                } else {
                    this.aStarMove();
                }
                // Go to the center's map
            } else {
                this.goCenterMapWithAStar();
            }

            // Just to the cont startMoveFps don't cont to not overload the system
        } else if (startMoveFps <= startMoveTime + 30) {
            startMoveFps++;
        }

        // The enemy will be invulnerable for some time after the player collides with it
        if (feedBack) {
            feedBackFps++;
            sparkleFeedBackFps++;
            if (sparkleFeedBackFps < 30) {
                mayRender = true;
            } else if (sparkleFeedBackFps < 40) {
                mayRender = false;
            } else {
                sparkleFeedBackFps = 0;
            }
            if (feedBackFps >= 120) {
                feedBackFps = 0;
                feedBack = false;
            }
        } else {
            mayRender = true;
        }
    }

    // Every time that ghostMode reach the limit, a new random limit will be been put
    private void ghostModeSystem() {
        ghostModeFps++;
        ghostMode = ghostModeFps < 2 * 60;

        if (ghostModeFps == randomTimeForGhostMode) {
            ghostModeFps = 0;
            randomTimeForGhostMode = new Random().nextInt(bound) + minTime;
            // ghostMode = !ghostMode;
        }
    }

    private void enemiesFindRandomPath() {
        if (curDir == rightDir) {
            if (Map.isFree(getX() + speed, getY(), this.speed, this.getWidth(), this.getHeight())) {
                this.setX(getX() + speed);
            } else {
                generateRandomPath();
            }
        } else if (curDir == leftDir) {
            if (Map.isFree(getX() - speed, getY(), this.speed, this.getWidth(), this.getHeight())) {
                this.setX(getX() - speed);
            } else {
                generateRandomPath();
            }
        } else if (curDir == downDir) {
            if (Map.isFree(getX(), getY() + speed, this.speed, this.getWidth(), this.getHeight())) {
                this.setY(getY() + speed);
            } else {
                generateRandomPath();
            }
        } else if (curDir == upDir) {
            if (Map.isFree(getX(), getY() - speed, this.speed, this.getWidth(), this.getHeight())) {
                this.setY(getY() - speed);
            } else {
                generateRandomPath();
            }
        }
    }

    private void aStarMove() {
        // If our path is null, we will try to find a available path
        if (path == null || path.size() == 0) {
            int xGoal = 0;
            int yGoal = 0;
            for (int i = 0; i < Game.entities.size(); i++) {
                Entity e = Game.entities.get(i);
                if (e instanceof PacMan) {
                    xGoal = e.getX();
                    yGoal = e.getY();
                    break;
                }
            }
            Vector2i start = new Vector2i(this.getX() / Map.tileSize, this.getY() / Map.tileSize);
            Vector2i end = new Vector2i(xGoal / Map.tileSize, yGoal / Map.tileSize);
            path = AStar.findPath(start, end);
        }

        if (new Random().nextInt(100) <= 80) {
            followPath4Dirs(path, this.speed, this);
        }

        if (new Random().nextInt(100) <= 80) {
            int xGoal = 0;
            int yGoal = 0;
            for (int i = 0; i < Game.entities.size(); i++) {
                Entity e = Game.entities.get(i);
                if (e instanceof PacMan) {
                    xGoal = e.getX();
                    yGoal = e.getY();
                    break;
                }
            }
            Vector2i start = new Vector2i(this.getX() / Map.tileSize, this.getY() / Map.tileSize);
            Vector2i end = new Vector2i(xGoal / Map.tileSize, yGoal / Map.tileSize);
            path = AStar.findPath(start, end);
        }
    }

    private void goCenterMapWithAStar() {
        // First we clear the last path
        if (erasePath) {
            erasePath = false;
            if (path != null) {
                path.clear();
            }
        }

        int xGoal = (Map.mapWidth) / 2;
        int yGoal = (Map.mapHeight) / 2;

        if (path == null || path.size() == 0) {
            Vector2i start = new Vector2i(this.getX() / Map.tileSize, this.getY() / Map.tileSize);
            Vector2i end = new Vector2i(xGoal, yGoal);
            path = AStar.findPath(start, end);
        }

        followPath4Dirs(path, 2, this);

        /* When they reach to the point (map's center) damaged will receive false and the path
         * may be erased again
         */
        if (((this.getX() >= xGoal * 15) && (this.getX() <= xGoal * 17))
                && ((this.getY() >= yGoal * 15) && (this.getY() <= yGoal * 17))) {
            damaged = false;
            feedBack = true;
        }
    }

    private void generateRandomPath() {
        curDir = 1 + new Random().nextInt(4);
    }

    private void sleepingMechanism() {
        if (!sleeping) {
            return;
        }

        zFps++;
        if (zFps < 30) {
            xZPos = 10;
            yZPos = 2;
            curF = downLetterZVerdana;
        } else if (zFps < 60) {
            xZPos = 11;
            yZPos = 0;
            curF = middleLetterZVerdana;
        } else if (zFps < 90) {
            xZPos = 12;
            yZPos = -2;
            curF = topLetterVerdana;
        } else {
            zFps = 0;
        }
    }

    public void render(Graphics g) {
        if (sleeping) {
            g.drawImage(redPhantomAnimations[4], this.getX() - Camera.x, this.getY() - Camera.y,
                    this.getWidth(), this.getHeight(), null);
            return;
        }

        if (damaged || !mayRender) {
            defaultRender(g, damagedPhantomAnimations);
        } else {
            defaultRender(g, redPhantomAnimations);
        }
    }

    private void defaultRender(Graphics g, BufferedImage[] animations) {
        int x = this.getX() - Camera.x;
        int y = this.getY() - Camera.y;

        if (curDir == rightDir) {
            g.drawImage(animations[0], x, y, this.getWidth(), this.getHeight(), null);
        } else if (curDir == leftDir) {
            g.drawImage(animations[1], x, y, this.getWidth(), this.getHeight(), null);
        } else if (curDir == downDir) {
            g.drawImage(animations[2], x, y, this.getWidth(), this.getHeight(), null);
        } else if (curDir == upDir) {
            g.drawImage(animations[3], x, y, this.getWidth(), this.getHeight(), null);
        }
    }

    public void renderSleep(GameEngine gameEngine, Graphics g) {
        if (sleeping) {
            g.setColor(new Color(255, 255, 255));
            g.setFont(curF);
            g.drawString("Z", (this.getX() + xZPos - Camera.x) * gameEngine.getRenderer().getSCALE(),
                    (this.getY() + yZPos - Camera.y) * gameEngine.getRenderer().getSCALE());
        }
    }

    public boolean isDamagedInverted() {
        return !damaged;
    }

    public boolean isFeedBackInverted() {
        return !feedBack;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void setCurDir(int curDir) {
        this.curDir = curDir;
    }

    public int getRightDir() {
        return rightDir;
    }

    public int getLeftDir() {
        return leftDir;
    }

    public int getDownDir() {
        return downDir;
    }

    public int getUpDir() {
        return upDir;
    }
}

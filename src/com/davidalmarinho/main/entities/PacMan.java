package com.davidalmarinho.main.entities;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.map.Camera;
import com.davidalmarinho.game_engine.map.Map;
import com.davidalmarinho.game_engine.utils.Sound;
import com.davidalmarinho.main.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class PacMan extends Entity {
    // Animations
    private BufferedImage[] pacRightDir;
    private BufferedImage[] pacLeftDir;
    private BufferedImage[] pacDownDir;
    private BufferedImage[] pacUpDir;
    private BufferedImage[] pacImmortalRightDir;
    private BufferedImage[] pacImmortalLeftDir;
    private BufferedImage[] pacImmortalDownDir;
    private BufferedImage[] pacImmortalUpDir;
    private int curFps = 0;
    private int index;
    // Movement
    private String curDir = "null";
    // Pac attributes
    public static int points = 0;
    public static int lives = 3;
    private boolean restartGame = false;
    private boolean immortal = false;
    private int immortalFps = 0;
    private int highScore = 0;

    public PacMan(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.speed = 1;
        setMask(0, 0, this.getWidth(), this.getHeight());
        depth = 2;
        this.loadSprites();
    }

    private void loadSprites() {
        pacRightDir = new BufferedImage[2];
        for (int i = 0; i < pacRightDir.length; i++) {
            pacRightDir[i] = GameEngine.spritesheet.getSprite(i * Map.tileSize, 2 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
        pacLeftDir = new BufferedImage[2];
        for (int i = 0; i < pacLeftDir.length; i++) {
            pacLeftDir[i] = GameEngine.spritesheet.getSprite((i + 2) * Map.tileSize, 2 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
        pacDownDir = new BufferedImage[2];
        for (int i = 0; i < pacDownDir.length; i++) {
            pacDownDir[i] = GameEngine.spritesheet.getSprite((i + 4) * Map.tileSize, 2 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
        pacUpDir = new BufferedImage[2];
        for (int i = 0; i < pacUpDir.length; i++) {
            pacUpDir[i] = GameEngine.spritesheet.getSprite((i + 6) * Map.tileSize, 2 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
        pacImmortalRightDir = new BufferedImage[2];
        for (int i = 0; i < pacImmortalRightDir.length; i++) {
            pacImmortalRightDir[i] = GameEngine.spritesheet.getSprite(i * Map.tileSize, 3 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
        pacImmortalLeftDir = new BufferedImage[2];
        for (int i = 0; i < pacImmortalLeftDir.length; i++) {
            pacImmortalLeftDir[i] = GameEngine.spritesheet.getSprite((i + 2) * Map.tileSize, 3 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
        pacImmortalDownDir = new BufferedImage[2];
        for (int i = 0; i < pacImmortalDownDir.length; i++) {
            pacImmortalDownDir[i] = GameEngine.spritesheet.getSprite((i + 4) * Map.tileSize, 3 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
        pacImmortalUpDir = new BufferedImage[2];
        for (int i = 0; i < pacImmortalUpDir.length; i++) {
            pacImmortalUpDir[i] = GameEngine.spritesheet.getSprite((i + 6) * Map.tileSize, 3 * Map.tileSize,
                    this.getWidth(), this.getHeight());
        }
    }

    public void tick(GameEngine gameEngine) {
        updateCamera(gameEngine);
        animationsSystem();
        this.movement(gameEngine);
        this.ballCollision();
        this.superBallCollision();
        if (!immortal)
            speed = 1;
        this.enemiesCollision();
    }

    protected void animationsSystem() {
        curFps++;
        int maxFps = 20;
        if (curFps > maxFps) {
            curFps = 0;
            index++;
        }

        int maxIndex = 2;
        if (index == maxIndex) {
            index = 0;
        }
    }

    private void movement(GameEngine gameEngine) {
        boolean rightCondition = Map.isFree(this.getX() + this.speed, this.getY(), this.speed,
                this.getWidth(), this.getHeight());
        boolean leftCondition = Map.isFree(this.getX() - this.speed, this.getY(), this.speed,
                this.getWidth(), this.getHeight());
        boolean downCondition = Map.isFree(this.getX(), this.getY() + this.speed, this.speed,
                this.getWidth(), this.getHeight());
        boolean upCondition = Map.isFree(this.getX(), this.getY() - this.speed, this.speed,
                this.getWidth(), this.getHeight());

        if (gameEngine.getInput().isKey(KeyEvent.VK_RIGHT) && rightCondition) {
            curDir = "right_dir";
        } else if (gameEngine.getInput().isKey(KeyEvent.VK_LEFT) && leftCondition) {
            curDir = "left_dir";
        } else if (gameEngine.getInput().isKey(KeyEvent.VK_DOWN) && downCondition) {
            curDir = "down_dir";
        } else if (gameEngine.getInput().isKey(KeyEvent.VK_UP) && upCondition) {
            curDir = "up_dir";
        }


        if (curDir.equals("right_dir") && rightCondition) {
            this.setX(this.getX() + this.speed);
        } else if (curDir.equals("left_dir") && leftCondition) {
            this.setX(this.getX() - this.speed);
        } else if (curDir.equals("down_dir") && downCondition) {
            this.setY(this.getY() + this.speed);
        } else if (curDir.equals("up_dir") && upCondition) {
            this.setY(this.getY() - this.speed);
        }
    }

    private void ballCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Ball) {
                if (isColliding(this, e)) {
                    points++;
                    Sound.pickBall.play();
                    Game.entities.remove(e);
                    break;
                }
            }
        }
    }

    private void superBallCollision() {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof SuperBall) {
                if (isColliding(this, e)) {
                    Sound.pickSuperBall.play();
                    immortal = true;
                    this.speed = 2;
                    immortalFps = 0;
                    Game.entities.remove(e);
                }
            }
        }
    }

    private void enemiesCollision() {
        if (immortal) {
            immortalFps++;
        }
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity e = Game.entities.get(i);
            if (e instanceof Phantom) {

                if (immortal && isColliding(this, e) && ((Phantom) e).isDamagedInverted() &&
                        ((Phantom) e).isFeedBackInverted()) {
                    ((Phantom) e).setDamaged(true);
                    Sound.hurt.play();
                    break;

                } else if (isColliding(this, e) && ((Phantom) e).isDamagedInverted() &&
                        !((Phantom) e).isSleeping() && ((Phantom) e).isFeedBackInverted()) {
                    Sound.hurt.play();
                    lives--;
                    if (lives != 0)
                        restartGame = true;
                }

                if (immortal) {
                    if (immortalFps >= 60 * 6) {
                        immortalFps = 0;
                        immortal = false;
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        int curX = this.getX() - Camera.x;
        int curY = this.getY() - Camera.y;
        if (!immortal) {
            switch (curDir) {
                case "left_dir":
                    g.drawImage(pacLeftDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
                case "down_dir":
                    g.drawImage(pacDownDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
                case "up_dir":
                    g.drawImage(pacUpDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
                default:
                    g.drawImage(pacRightDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
            }
        } else {
            switch (curDir) {
                case "left_dir":
                    g.drawImage(pacImmortalLeftDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
                case "down_dir":
                    g.drawImage(pacImmortalDownDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
                case "up_dir":
                    g.drawImage(pacImmortalUpDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
                default:
                    g.drawImage(pacImmortalRightDir[index], curX, curY, this.getWidth(), this.getHeight(), null);
                    break;
            }
        }
    }

    public boolean isRestartGame() {
        return restartGame;
    }

    public void setRestartGame(boolean restartGame) {
        this.restartGame = restartGame;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }
}

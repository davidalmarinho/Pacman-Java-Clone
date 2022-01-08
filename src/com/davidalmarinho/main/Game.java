package com.davidalmarinho.main;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.engine_core.GameOver;
import com.davidalmarinho.game_engine.engine_core.GamePlan;
import com.davidalmarinho.game_engine.map.Map;
import com.davidalmarinho.game_engine.utils.Sound;
import com.davidalmarinho.game_engine.utils.Utils;
import com.davidalmarinho.main.entities.Ball;
import com.davidalmarinho.main.entities.Entity;
import com.davidalmarinho.main.entities.PacMan;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.awt.*;

public class Game implements GamePlan {
    private final UserInterface ui;
    public static List<Entity> entities;
    private int curLevel = 1;

    public Game() {
         entities = new ArrayList<>();
         ui = new UserInterface();
    }

    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.start();
    }

    @Override
    public void tick(GameEngine gameEngine) {
        Sound.gameMusic.loopLessLoudSongs();
        if (gameEngine.getGameState().equals("normal")) {
            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                e.tick(gameEngine);

                if (e instanceof PacMan) {
                    this.nextLevel(gameEngine, (PacMan) e);
                }
            }
            this.gameOver(gameEngine);
        }

        this.pauseMenu(gameEngine);
        if (gameEngine.getGameState().equals("menu")) {
            PacMan.lives = 3;
            PacMan.points = 0;
        }
    }

    private void nextLevel(GameEngine gameEngine, PacMan pacMan) {
        if (!isBalls()) {
            curLevel ++;
            int maxLevel = 3;
            if (curLevel > maxLevel) {
                curLevel = 1;
            }
            Map.restartGame(gameEngine, curLevel);
        } else if (pacMan.isRestartGame()) {
            pacMan.setRestartGame(false);
            Map.restartGame(gameEngine, curLevel);
        }
    }

    private boolean isBalls() {
        for (int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            if (e instanceof Ball) {
                return true;
            }
        }
        return false;
    }

    private void gameOver(GameEngine gameEngine) {
        if (PacMan.lives == 0) {
            curLevel = 1;

            for (int i = 0; i < entities.size(); i++) {
                Entity entity = entities.get(i);

                if (entity instanceof PacMan) {
                    this.loadSavedHighScore();

                    if (((PacMan) entity).getHighScore() < PacMan.points) {
                        ((PacMan) entity).setHighScore(PacMan.points);
                        this.saveHighScore((PacMan) entity);
                        break;
                    }
                }
            }
            gameEngine.setGameState("game_over");
            gameEngine.setGameOver(new GameOver());
        }
    }

    private void loadSavedHighScore() {
        File file = new File("save.txt");
        if (file.exists()) {
            String saver = Utils.loadGame(20);
            Utils.applySaver(saver);
        }
    }

    private void saveHighScore(PacMan pacMan) {
        String[] items = {"high_score"};
        int[] valueOfItems = {pacMan.getHighScore()};
        Utils.saveGame(items, valueOfItems, 20);
    }

    private void pauseMenu(GameEngine gameEngine) {
        if (gameEngine.getGameState().equals("normal") || gameEngine.getGameState().equals("pause_menu")) {
            if (gameEngine.getInput().isKeyDown(KeyEvent.VK_ESCAPE)) {
                gameEngine.getPauseMenu().setPauseMenu(!gameEngine.getPauseMenu().isPauseMenu());
            } else if (gameEngine.getInput().isKeyDown(KeyEvent.VK_ENTER)) {
                gameEngine.getPauseMenu().setPauseMenu(false);
            }
        }
    }

    @Override
    public void renderPixelatedBackground(Graphics g) {
        entities.sort(Entity.entitySorter);
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).render(g);
        }
    }

    @Override
    public void renderUIBackground(GameEngine gameEngine, Graphics g) {
        ui.render(gameEngine, g);
    }

    public int getCurLevel() {
        return curLevel;
    }
}

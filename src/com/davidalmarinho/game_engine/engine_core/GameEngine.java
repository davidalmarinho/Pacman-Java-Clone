package com.davidalmarinho.game_engine.engine_core;

import com.davidalmarinho.game_engine.menu.Menu;
import com.davidalmarinho.game_engine.menu.PauseMenu;
import com.davidalmarinho.game_engine.utils.Input;
import com.davidalmarinho.game_engine.map.Map;
import com.davidalmarinho.game_engine.map.tiles.Spritesheet;
import com.davidalmarinho.main.Game;

import java.awt.event.KeyEvent;

public class GameEngine implements Runnable {
    // Core
    private final GamePlan gamePlan;
    private Thread thread;
    private boolean running = true;
    private String gameState = "game_introduction";
    // Components
    private Renderer renderer;
    private boolean fullScreen = false;
    private boolean mayMakeChanges = false;
    private Input input;
    private final GameIntro gameIntro;
    private final Menu menu;
    private final PauseMenu pauseMenu;
    private Map map;
    public static boolean errorOccurred = false;
    // Spritesheet
    public static Spritesheet spritesheet;
    private GameOver gameOver;
    private boolean debug = false;

    public GameEngine() {
        renderer = new Renderer(this);
        input = new Input(this);
        spritesheet = new Spritesheet();
        gameIntro = new GameIntro();
        menu = new Menu();
        pauseMenu = new PauseMenu();
        Game game = new Game();
        gamePlan = game;
        map = new Map(game.getCurLevel());
        gameOver = new GameOver();
    }

    /**
     * A tick to set the user settings
     */
    private void tickSetConfig() {
        if (input.isKeyDown(KeyEvent.VK_F3)) {
            this.debug = true;
        }
        if (input.isKey(KeyEvent.VK_F)) {
            mayMakeChanges = true;
            fullScreen = !fullScreen;
            renderer.getJFrame().dispose();
            renderer = new Renderer(this);
            input = new Input(this);
        }
    }

    private void tickComponents() {
        gameIntro.tick(this);
        menu.tick(this);
        pauseMenu.tick(this);
        gameOver.tick(this);
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
    }

    private synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.gameLooping();
    }

    private void gameLooping() {
        long lastTime = System.nanoTime();
        double maxFps = 60.0;
        double ns = 1000000000 / maxFps;
        double delta = 0;
        // FPS Controller
        double lastTimeFPS = System.currentTimeMillis();
        int ticks = 0;
        int fps = 0;

        while (running) {
            long curTime = System.nanoTime();
            delta += (curTime - lastTime) / ns;
            lastTime = curTime;

            if (delta >= 1) {
                this.tickComponents();
                gamePlan.tick(this);
                this.tickSetConfig();
                input.tick();
                renderer.render(gamePlan, this);
                ticks++;
                delta--;
            }
            fps++;

            if (System.currentTimeMillis() - lastTimeFPS >= 1000) {
                if (debug) {
                    System.out.println("ticks " + ticks + ", fps " + fps);
                }
                ticks = 0;
                fps = 0;
                lastTimeFPS += 1000;
            }
        }
        this.stop();
    }

    public Input getInput() {
        return input;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public boolean isFullScreen() {
        return fullScreen;
    }

    public boolean isMayMakeChanges() {
        return mayMakeChanges;
    }

    public void setMayMakeChanges(boolean mayMakeChanges) {
        this.mayMakeChanges = mayMakeChanges;
    }

    public GameIntro getGameIntro() {
        return gameIntro;
    }

    public Menu getMenu() {
        return menu;
    }

    public PauseMenu getPauseMenu() {
        return pauseMenu;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    public void setGameOver(GameOver gameOver) {
        this.gameOver = gameOver;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}

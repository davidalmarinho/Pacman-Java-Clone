package com.davidalmarinho.game_engine.engine_core;

import com.davidalmarinho.game_engine.map.Map;
import com.davidalmarinho.game_engine.utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Renderer extends Canvas {
    private JFrame jFrame;
    private final int SCALE = 2;
    private final int HEIGHT = 480 / SCALE;
    private final int WIDTH = 640 / SCALE;
    private final BufferedImage pixelatedBackground;
    private final BufferedImage uiBackground;

    public Renderer(GameEngine gameEngine) {
        this.renderWindow(gameEngine);
        pixelatedBackground = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        uiBackground = new BufferedImage(WIDTH * SCALE, HEIGHT * SCALE,
                BufferedImage.TYPE_INT_ARGB);
    }

    private void renderWindow(GameEngine gameEngine) {
        createWindow(gameEngine);
        requestFocus();
    }

    /**
     * Create a Window based in if we want fullscreen or not
     * @param gameEngine Here we will pick the
     *                   fullscreen's and the mayMakeChanges's booleans
     */
    private void createWindow(GameEngine gameEngine) {
        String titleGame = "PacMan";
        if (gameEngine.isMayMakeChanges()) {
            gameEngine.setMayMakeChanges(false);
        }
        jFrame = new JFrame(titleGame);
        jFrame.setResizable(false);

        if (gameEngine.isFullScreen()) {
            if (Utils.isLinux() && (!jFrame.isMaximumSizeSet())) {
                jFrame.setPreferredSize(new Dimension
                        (Toolkit.getDefaultToolkit().getScreenSize()));
            } else {
                jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                jFrame.setUndecorated(true);
                jFrame.setLocationRelativeTo(null);
            }
        } else {
            jFrame.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
        }

        jFrame.add(this);
        jFrame.pack();
        this.setCursor();
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true);
    }

    private void setCursor() {
        BufferedImage imgCursor = new BufferedImage(Map.tileSize, Map.tileSize, BufferedImage.TYPE_INT_ARGB);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor newCursor = toolkit.createCustomCursor(imgCursor, new Point(0, 0), "cursor");
        jFrame.setCursor(newCursor);
    }

    /**
     * Render the canvas that we will paint in our 2 painting canvas
     * @param gamePlan To access to the render methods from GamePlan
     * @param gameEngine To indicate if we will render in fullscreen mode
     */
    public void render(GamePlan gamePlan, GameEngine gameEngine) {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        this.renderPixelatedBackground(bs, gamePlan, gameEngine);
        this.renderUIBackgrounds(bs, gamePlan, gameEngine);
        bs.show();
    }

    private void renderPixelatedBackground(BufferStrategy bs, GamePlan gamePlan, GameEngine gameEngine) {
        Graphics g = pixelatedBackground.getGraphics();
        g.setColor(new Color(0 ,0, 0));
        g.fillRect(0 ,0, WIDTH, HEIGHT);
        this.renderMap(g, gameEngine);
        gamePlan.renderPixelatedBackground(g);
        g.dispose();
        g = bs.getDrawGraphics();
        renderBackgroundInFullscreenOrNot(gameEngine, g, pixelatedBackground);
    }

    private void renderMap(Graphics g, GameEngine gameEngine) {
        gameEngine.getMap().render(g, gameEngine);
    }

    private void renderUIBackgrounds(BufferStrategy bs, GamePlan gamePlan, GameEngine gameEngine) {
        Graphics2D g2D = (Graphics2D) uiBackground.getGraphics();
        g2D.setBackground(new Color(0, true));
        g2D.clearRect(0, 0, WIDTH * SCALE, HEIGHT * SCALE);
        gamePlan.renderUIBackground(gameEngine, g2D);
        if (GameEngine.errorOccurred) {
            g2D.setColor(new Color(255, 0, 0));
            g2D.setFont(new Font("arial", Font.BOLD, 24));
            g2D.drawString("Exception occurred, please contact the developer!", 16, 24);
        }
        gameEngine.getMenu().render(g2D, gameEngine);
        gameEngine.getPauseMenu().render(gameEngine, g2D);
        gameEngine.getGameIntro().render(gameEngine, g2D);
        gameEngine.getGameOver().render(gameEngine, g2D);
        g2D.dispose();
        g2D = (Graphics2D) bs.getDrawGraphics();
        renderBackgroundInFullscreenOrNot(gameEngine, g2D,uiBackground);
    }

    private void renderBackgroundInFullscreenOrNot(GameEngine gameEngine, Graphics g, BufferedImage background) {
        if (gameEngine.isFullScreen() && Utils.isLinux() && !jFrame.isMaximumSizeSet()) {
            g.drawImage(background, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
                    Toolkit.getDefaultToolkit().getScreenSize().height, null);
        } else if (gameEngine.isFullScreen()) {
            int width = jFrame.getWidth();
            int height = jFrame.getHeight();
            g.drawImage(background, 0, 0, width,
                    height, null);
        } else {
            g.drawImage(background, 0, 0,
                    WIDTH * SCALE, HEIGHT * SCALE, null);
        }
    }

    public JFrame getJFrame() {
        return jFrame;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getSCALE() {
        return SCALE;
    }
}

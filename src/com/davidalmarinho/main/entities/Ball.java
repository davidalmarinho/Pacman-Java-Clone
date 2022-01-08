package com.davidalmarinho.main.entities;

import com.davidalmarinho.game_engine.map.Camera;

import java.awt.*;

public class Ball extends Entity {
    private final int centerTheBall = 6;

    public Ball(int x, int y, int width, int height) {
        super(x, y, width, height);
        setMask(centerTheBall, centerTheBall, this.getWidth(), this.getHeight());
        depth = 0;
    }

    public void render(Graphics g) {
        g.setColor(new Color(255, 255, 0));
        g.fillOval(this.getX() + centerTheBall - Camera.x, this.getY() + centerTheBall - Camera.y,
                this.getWidth(), this.getHeight());
    }
}

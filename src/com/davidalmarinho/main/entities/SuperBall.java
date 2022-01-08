package com.davidalmarinho.main.entities;

import com.davidalmarinho.game_engine.map.Camera;

import java.awt.*;
import java.util.Random;

public class SuperBall extends Entity {
    private final int centerTheSuperBall = 5;

    public SuperBall(int x, int y, int width, int height) {
        super(x, y, width, height);
        setMask(centerTheSuperBall, centerTheSuperBall, this.getWidth(), this.getHeight());
        depth = 0;
    }

    public void render(Graphics g) {
        g.setColor(new Color(new Random().nextInt(256), new Random().nextInt(256),
                new Random().nextInt(256)));
        g.fillOval(this.getX() + centerTheSuperBall - Camera.x,
                this.getY() + centerTheSuperBall - Camera.y,
                this.getWidth(), this.getHeight());
    }
}

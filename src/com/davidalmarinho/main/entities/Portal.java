package com.davidalmarinho.main.entities;

import com.davidalmarinho.game_engine.engine_core.GameEngine;
import com.davidalmarinho.game_engine.utils.Sound;
import com.davidalmarinho.main.Game;

import java.awt.*;

public class Portal extends Entity {
    private final String dirLocation;

    public Portal(int x, int y, int width, int height, String dirLocation) {
        super(x, y, width, height);
        this.dirLocation = dirLocation;
    }

    public void tick(GameEngine gameEngine) {
        for (int i = 0; i < Game.entities.size(); i++) {
            Entity entity = Game.entities.get(i);
            if (entity instanceof PacMan || entity instanceof Phantom) {
                this.teleportToLeft(entity);
                this.teleportToRight(entity);
            }
        }
    }

    // Teleport the Player to the right side of the map
    private void teleportToRight(Entity entity) {
        if (dirLocation.equals("left")) {
            for (int i = 0; i < Game.entities.size(); i++) {
                Entity port = Game.entities.get(i);
                if (port instanceof Portal) {
                    if (((Portal) port).dirLocation.equals("right")) {
                        setMask(0, 0, this.getWidth(), this.getHeight());
                        if (isColliding(this, entity)) {
                            Sound.enterPortal.play();
                            entity.setX(port.getX() - 1);
                            entity.setY(port.getY());
                        }
                    }
                }
            }
        }
    }

    // Teleport the Player to the left side of the map
    private void teleportToLeft(Entity entity) {
        if (dirLocation.equals("right")) {
            for (int i = 0; i < Game.entities.size(); i++) {
                Entity port = Game.entities.get(i);
                if (port instanceof Portal) {
                    if (((Portal) port).dirLocation.equals("left")) {
                        setMask(15, 0, this.getWidth(), this.getHeight());
                        if (isColliding(this, entity)) {
                            Sound.enterPortal.play();
                            entity.setX(port.getX());
                            entity.setY(port.getY());
                        }
                    }
                }
            }
        }
    }

    public void render(Graphics g) {
        seeMask(g);
    }
}

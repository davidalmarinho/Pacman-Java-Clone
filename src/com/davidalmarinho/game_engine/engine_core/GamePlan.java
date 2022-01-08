package com.davidalmarinho.game_engine.engine_core;

import java.awt.*;

public interface GamePlan {
    void tick(GameEngine gameEngine);
    void renderPixelatedBackground(Graphics g);
    void renderUIBackground(GameEngine gameEngine, Graphics g);
}

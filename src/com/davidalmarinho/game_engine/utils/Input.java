package com.davidalmarinho.game_engine.utils;

import com.davidalmarinho.game_engine.engine_core.GameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Input implements KeyListener {
    private final int numberOfKeys = 526; // 256
    private final boolean[] keys = new boolean[numberOfKeys];
    private final boolean[] keysLastNumber = new boolean[numberOfKeys];

    public Input(GameEngine gameEngine) {
        gameEngine.getRenderer().addKeyListener(this);
    }

    public void tick() {
        System.arraycopy(keys, 0, keysLastNumber, 0, numberOfKeys);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }

    public boolean isKeyDown(int keyCode) {
        return keys[keyCode] && !keysLastNumber[keyCode];
    }

    public boolean isKeyUp(int keyCode) {
        return !keys[keyCode] && keysLastNumber[keyCode];
    }

    public boolean isKey(int keyCode) {
        return keys[keyCode];
    }
}

package com.davidalmarinho.game_engine.a_star;

public class Vector2i {
    public int x, y;

    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isEquals(Object object) {
        Vector2i vec = (Vector2i) object;
        return (vec.x == this.x) && (vec.y == y);
    }
}

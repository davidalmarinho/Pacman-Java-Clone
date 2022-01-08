package com.davidalmarinho.game_engine.a_star;

public class Node {
    public Vector2i originOfThePath;
    public Node parent;
    public double gCost, hCost, fCost;

    public Node(Vector2i vector2i, Node parent, double gCost, double hCost) {
        this.originOfThePath = vector2i;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }
}

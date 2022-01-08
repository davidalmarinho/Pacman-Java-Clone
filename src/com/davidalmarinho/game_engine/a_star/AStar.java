package com.davidalmarinho.game_engine.a_star;

import com.davidalmarinho.game_engine.map.Map;
import com.davidalmarinho.game_engine.map.tiles.Tile;
import com.davidalmarinho.game_engine.map.tiles.WallTile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AStar {
    public static long lastTime = System.currentTimeMillis();

    public static List<Node> findPath(Vector2i start, Vector2i end) {
        lastTime = System.currentTimeMillis();
        List<Node> openList = new ArrayList<>();
        List<Node> closedList = new ArrayList<>();

        Node current = new Node(start, null, 0, getDistanceBetweenNodes(start, end));
        openList.add(current);
        while (openList.size() > 0) {
            openList.sort(nodeSorter);
            current = openList.get(0);
            if (current.originOfThePath.isEquals(end)) {
                List<Node> path = new ArrayList<>();
                while (current.parent != null) {
                    path.add(current);
                    current = current.parent;
                }
                openList.clear();
                closedList.clear();
                return path;
            }

            openList.remove(current);
            closedList.add(current);

            for (int i = 0; i < 9; i++) {
                if (i == 4) {
                    continue;
                }

                int x = current.originOfThePath.x;
                int y = current.originOfThePath.y;
                int xI = (i % 3) - 1;
                int yI = (i / 3) - 1;

                Tile tile = Map.tiles[x + xI + (y + yI) * Map.mapWidth];
                if (tile == null) {
                    continue;
                }

                if (tile instanceof WallTile) {
                    continue;
                }

                if (i == 0) {
                    Tile verify0 = Map.tiles[x + xI + 1 + ((y + yI) * Map.mapWidth)];
                    Tile verify1 = Map.tiles[x + xI + ((y + yI + 1) * Map.mapWidth)];

                    // This will possibly the enemy walk in diagonals too
                    if (verify0 instanceof WallTile || verify1 instanceof WallTile) {
                        continue;
                    }

                } else if (i == 2) {
                    Tile verify0 = Map.tiles[x + xI - 1 + ((y + yI) * Map.mapWidth)];
                    Tile verify1 = Map.tiles[x + xI + ((y + yI + 1) * Map.mapWidth)];
                    if (verify0 instanceof WallTile || verify1 instanceof WallTile) {
                        continue;
                    }

                } else if (i == 6) {
                    Tile verify0 = Map.tiles[x + xI + ((y + yI - 1) * Map.mapWidth)];
                    Tile verify1 = Map.tiles[x + xI + 1 + ((y + yI) * Map.mapWidth)];
                    if (verify0 instanceof WallTile || verify1 instanceof  WallTile) {
                        continue;
                    }

                } else if (i == 8) {
                    Tile verify0 = Map.tiles[x + xI + ((y + yI - 1) * Map.mapWidth)];
                    Tile verify1 = Map.tiles[x + xI - 1 + ((y + yI) * Map.mapWidth)];
                    if (verify0 instanceof WallTile || verify1 instanceof WallTile) {
                        continue;
                    }
                }

                Vector2i a = new Vector2i(x + xI, y + yI);
                double gCost = current.gCost + getDistanceBetweenNodes(current.originOfThePath, a);
                double hCost = getDistanceBetweenNodes(a, end);

                Node node = new Node(a, current, gCost, hCost);

                if (isVecInList(closedList, a) && (gCost >= current.gCost)) {
                    continue;
                }

                if (!isVecInList(openList, a)) {
                    openList.add(node);
                } else if (gCost < current.gCost) {
                    openList.remove(current);
                    openList.add(node);
                }
            }
        }
        closedList.clear();
        return null;
    }

    // To sort the node
    private static final Comparator<Node> nodeSorter = Comparator.comparingDouble(o -> o.fCost);


    private static double getDistanceBetweenNodes(Vector2i start, Vector2i end) {
        double dX = start.x - end.x;
        double dY = start.y - end.y;
        return Math.sqrt(dX * dX + dY * dY);
    }

    private static boolean isVecInList(List<Node> nodeList, Vector2i vector2i) {
        for (int i = 0; i < nodeList.size(); i++) {
            if (nodeList.get(i).originOfThePath.isEquals(vector2i)) {
                return true;
            }
        }
        return false;
    }
}

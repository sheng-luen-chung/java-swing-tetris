package tetris;

import java.awt.Color;

public enum Shape {
    I(new int[][] {{0, 1}, {1, 1}, {2, 1}, {3, 1}}, new Color(0, 188, 212)),
    O(new int[][] {{1, 0}, {2, 0}, {1, 1}, {2, 1}}, new Color(255, 193, 7)),
    T(new int[][] {{1, 0}, {0, 1}, {1, 1}, {2, 1}}, new Color(156, 39, 176)),
    S(new int[][] {{1, 0}, {2, 0}, {0, 1}, {1, 1}}, new Color(76, 175, 80)),
    Z(new int[][] {{0, 0}, {1, 0}, {1, 1}, {2, 1}}, new Color(244, 67, 54)),
    J(new int[][] {{0, 0}, {0, 1}, {1, 1}, {2, 1}}, new Color(63, 81, 181)),
    L(new int[][] {{2, 0}, {0, 1}, {1, 1}, {2, 1}}, new Color(255, 152, 0));

    private final int[][] blocks;
    private final Color color;

    Shape(int[][] blocks, Color color) {
        this.blocks = blocks;
        this.color = color;
    }

    public int[][] getBlocks() {
        int[][] copy = new int[blocks.length][2];
        for (int i = 0; i < blocks.length; i++) {
            copy[i][0] = blocks[i][0];
            copy[i][1] = blocks[i][1];
        }
        return copy;
    }

    public Color getColor() {
        return color;
    }
}

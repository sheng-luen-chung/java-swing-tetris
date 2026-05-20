package tetris;

import java.awt.Color;

public class Tetromino {
    private final Shape shape;
    private int[][] blocks;
    private int x;
    private int y;

    public Tetromino(Shape shape, int x, int y) {
        this.shape = shape;
        this.blocks = shape.getBlocks();
        this.x = x;
        this.y = y;
    }

    private Tetromino(Shape shape, int[][] blocks, int x, int y) {
        this.shape = shape;
        this.blocks = blocks;
        this.x = x;
        this.y = y;
    }

    public Tetromino movedBy(int dx, int dy) {
        return new Tetromino(shape, copyBlocks(), x + dx, y + dy);
    }

    public Tetromino rotatedClockwise() {
        if (shape == Shape.O) {
            return new Tetromino(shape, copyBlocks(), x, y);
        }

        int[][] rotated = new int[blocks.length][2];
        for (int i = 0; i < blocks.length; i++) {
            int oldX = blocks[i][0];
            int oldY = blocks[i][1];
            rotated[i][0] = 3 - oldY;
            rotated[i][1] = oldX;
        }
        return new Tetromino(shape, normalize(rotated), x, y);
    }

    private int[][] normalize(int[][] source) {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        for (int[] block : source) {
            minX = Math.min(minX, block[0]);
            minY = Math.min(minY, block[1]);
        }

        int[][] normalized = new int[source.length][2];
        for (int i = 0; i < source.length; i++) {
            normalized[i][0] = source[i][0] - minX;
            normalized[i][1] = source[i][1] - minY;
        }
        return normalized;
    }

    private int[][] copyBlocks() {
        int[][] copy = new int[blocks.length][2];
        for (int i = 0; i < blocks.length; i++) {
            copy[i][0] = blocks[i][0];
            copy[i][1] = blocks[i][1];
        }
        return copy;
    }

    public int[][] getBlocks() {
        return copyBlocks();
    }

    public Color getColor() {
        return shape.getColor();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

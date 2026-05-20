package tetris;

import java.awt.Color;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    private final Color[][] cells = new Color[HEIGHT][WIDTH];

    public void clear() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                cells[row][col] = null;
            }
        }
    }

    public boolean canPlace(Tetromino tetromino) {
        for (int[] block : tetromino.getBlocks()) {
            int boardX = tetromino.getX() + block[0];
            int boardY = tetromino.getY() + block[1];

            if (boardX < 0 || boardX >= WIDTH || boardY < 0 || boardY >= HEIGHT) {
                return false;
            }

            if (cells[boardY][boardX] != null) {
                return false;
            }
        }
        return true;
    }

    public void lock(Tetromino tetromino) {
        for (int[] block : tetromino.getBlocks()) {
            int boardX = tetromino.getX() + block[0];
            int boardY = tetromino.getY() + block[1];

            if (boardY >= 0 && boardY < HEIGHT && boardX >= 0 && boardX < WIDTH) {
                cells[boardY][boardX] = tetromino.getColor();
            }
        }
    }

    public int clearCompletedLines() {
        int cleared = 0;

        for (int row = HEIGHT - 1; row >= 0; row--) {
            if (isLineFull(row)) {
                removeLine(row);
                cleared++;
                row++;
            }
        }

        return cleared;
    }

    private boolean isLineFull(int row) {
        for (int col = 0; col < WIDTH; col++) {
            if (cells[row][col] == null) {
                return false;
            }
        }
        return true;
    }

    private void removeLine(int line) {
        for (int row = line; row > 0; row--) {
            for (int col = 0; col < WIDTH; col++) {
                cells[row][col] = cells[row - 1][col];
            }
        }

        for (int col = 0; col < WIDTH; col++) {
            cells[0][col] = null;
        }
    }

    public Color getCell(int row, int col) {
        return cells[row][col];
    }
}

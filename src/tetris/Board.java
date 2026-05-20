package tetris;

import java.awt.Color;

public class Board {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 20;

    private final Color[][] cells = new Color[HEIGHT][WIDTH];

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

    public Color getCell(int row, int col) {
        return cells[row][col];
    }
}

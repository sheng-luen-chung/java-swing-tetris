package tetris;

import java.util.Random;

public class GameEngine {
    private final Board board = new Board();
    private final Random random = new Random();
    private Tetromino currentPiece;
    private boolean gameOver;

    public GameEngine() {
        spawnNextPiece();
    }

    public void tick() {
        if (gameOver) {
            return;
        }

        Tetromino moved = currentPiece.movedBy(0, 1);
        if (board.canPlace(moved)) {
            currentPiece = moved;
            return;
        }

        board.lock(currentPiece);
        spawnNextPiece();
    }

    public void moveLeft() {
        tryMove(currentPiece.movedBy(-1, 0));
    }

    public void moveRight() {
        tryMove(currentPiece.movedBy(1, 0));
    }

    public void moveDown() {
        tick();
    }

    public void rotate() {
        tryMove(currentPiece.rotatedClockwise());
    }

    private void tryMove(Tetromino candidate) {
        if (!gameOver && board.canPlace(candidate)) {
            currentPiece = candidate;
        }
    }

    private void spawnNextPiece() {
        Shape[] shapes = Shape.values();
        Shape shape = shapes[random.nextInt(shapes.length)];
        currentPiece = new Tetromino(shape, Board.WIDTH / 2 - 2, 0);
        gameOver = !board.canPlace(currentPiece);
    }

    public Board getBoard() {
        return board;
    }

    public Tetromino getCurrentPiece() {
        return currentPiece;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}

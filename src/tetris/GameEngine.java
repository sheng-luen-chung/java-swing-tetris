package tetris;

import java.util.Random;

public class GameEngine {
    private static final int BASE_DROP_DELAY = 500;
    private static final int MIN_DROP_DELAY = 100;
    private static final int LEVEL_SPEED_STEP = 45;

    private final Board board = new Board();
    private final ScoreManager scoreManager = new ScoreManager();
    private final AudioManager audioManager = new AudioManager();
    private final Random random = new Random();
    private Tetromino currentPiece;
    private Tetromino nextPiece;
    private GameState state = GameState.RUNNING;

    public GameEngine() {
        restart();
    }

    public void tick() {
        if (state != GameState.RUNNING) {
            return;
        }

        movePieceDownOrLock();
    }

    public void moveLeft() {
        tryMove(currentPiece.movedBy(-1, 0));
    }

    public void moveRight() {
        tryMove(currentPiece.movedBy(1, 0));
    }

    public void softDrop() {
        if (state == GameState.RUNNING) {
            movePieceDownOrLock();
        }
    }

    public void hardDrop() {
        if (state != GameState.RUNNING) {
            return;
        }

        Tetromino dropped = currentPiece;
        while (board.canPlace(dropped.movedBy(0, 1))) {
            dropped = dropped.movedBy(0, 1);
        }

        currentPiece = dropped;
        audioManager.playDropSound();
        lockCurrentPiece();
    }

    public void rotate() {
        tryMove(currentPiece.rotatedClockwise());
    }

    public void togglePause() {
        if (state == GameState.RUNNING) {
            state = GameState.PAUSED;
        } else if (state == GameState.PAUSED) {
            state = GameState.RUNNING;
        }
    }

    public void restart() {
        board.clear();
        scoreManager.reset();
        audioManager.setMusicLevel(scoreManager.getLevel());
        nextPiece = createRandomPiece();
        state = GameState.RUNNING;
        spawnNextPiece();
    }

    private void movePieceDownOrLock() {
        Tetromino moved = currentPiece.movedBy(0, 1);
        if (board.canPlace(moved)) {
            currentPiece = moved;
            return;
        }

        lockCurrentPiece();
    }

    private void lockCurrentPiece() {
        board.lock(currentPiece);
        int clearedLines = board.clearCompletedLines();
        if (clearedLines > 0) {
            audioManager.playLineClearSound();
        }
        scoreManager.addClearedLines(clearedLines);
        audioManager.setMusicLevel(scoreManager.getLevel());
        spawnNextPiece();
    }

    private void tryMove(Tetromino candidate) {
        if (state == GameState.RUNNING && board.canPlace(candidate)) {
            currentPiece = candidate;
        }
    }

    private void spawnNextPiece() {
        currentPiece = nextPiece;
        nextPiece = createRandomPiece();

        if (!board.canPlace(currentPiece)) {
            state = GameState.GAME_OVER;
        }
    }

    private Tetromino createRandomPiece() {
        Shape[] shapes = Shape.values();
        Shape shape = shapes[random.nextInt(shapes.length)];
        return new Tetromino(shape, Board.WIDTH / 2 - 2, 0);
    }

    public int getDropDelay() {
        int delay = BASE_DROP_DELAY - (scoreManager.getLevel() - 1) * LEVEL_SPEED_STEP;
        return Math.max(MIN_DROP_DELAY, delay);
    }

    public Board getBoard() {
        return board;
    }

    public Tetromino getCurrentPiece() {
        return currentPiece;
    }

    public Tetromino getNextPiece() {
        return nextPiece;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    public GameState getState() {
        return state;
    }

    public AudioManager getAudioManager() {
        return audioManager;
    }

    public void toggleMusic() {
        audioManager.toggleMusic();
    }

    public boolean isGameOver() {
        return state == GameState.GAME_OVER;
    }
}

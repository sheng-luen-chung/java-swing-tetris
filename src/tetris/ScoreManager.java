package tetris;

public class ScoreManager {
    private int score;
    private int linesCleared;
    private int level;

    public ScoreManager() {
        reset();
    }

    public void reset() {
        score = 0;
        linesCleared = 0;
        level = 1;
    }

    public void addClearedLines(int lines) {
        if (lines <= 0) {
            return;
        }

        linesCleared += lines;
        score += getLineScore(lines) * level;
        level = linesCleared / 10 + 1;
    }

    private int getLineScore(int lines) {
        switch (lines) {
            case 1:
                return 100;
            case 2:
                return 300;
            case 3:
                return 500;
            case 4:
                return 800;
            default:
                return 0;
        }
    }

    public int getScore() {
        return score;
    }

    public int getLinesCleared() {
        return linesCleared;
    }

    public int getLevel() {
        return level;
    }
}

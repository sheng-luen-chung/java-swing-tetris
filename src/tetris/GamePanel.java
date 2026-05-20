package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private static final int CELL_SIZE = 30;
    private static final int SIDE_PANEL_WIDTH = 170;
    private static final int PREVIEW_CELL_SIZE = 20;

    private final GameEngine engine = new GameEngine();
    private final Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(Board.WIDTH * CELL_SIZE + SIDE_PANEL_WIDTH, Board.HEIGHT * CELL_SIZE));
        setBackground(new Color(24, 24, 28));
        setFocusable(true);

        timer = new Timer(engine.getDropDelay(), event -> {
            engine.tick();
            ((Timer) event.getSource()).setDelay(engine.getDropDelay());
            repaint();
        });
        timer.start();

        addKeyListener(new InputHandler(engine, () -> {
            timer.setDelay(engine.getDropDelay());
            repaint();
        }));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawBoard(g2);
        drawPiece(g2, engine.getCurrentPiece());
        drawGrid(g2);
        drawSidePanel(g2);

        if (engine.getState() == GameState.PAUSED) {
            drawOverlay(g2, "Paused", "Press P to resume");
        } else if (engine.getState() == GameState.GAME_OVER) {
            drawOverlay(g2, "Game Over", "Press R to restart");
        }

        g2.dispose();
    }

    private void drawBoard(Graphics2D g2) {
        Board board = engine.getBoard();
        for (int row = 0; row < Board.HEIGHT; row++) {
            for (int col = 0; col < Board.WIDTH; col++) {
                Color color = board.getCell(row, col);
                if (color != null) {
                    drawCell(g2, col, row, color);
                }
            }
        }
    }

    private void drawPiece(Graphics2D g2, Tetromino piece) {
        if (piece == null) {
            return;
        }

        for (int[] block : piece.getBlocks()) {
            drawCell(g2, piece.getX() + block[0], piece.getY() + block[1], piece.getColor());
        }
    }

    private void drawCell(Graphics2D g2, int col, int row, Color color) {
        int x = col * CELL_SIZE;
        int y = row * CELL_SIZE;

        g2.setColor(color);
        g2.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);

        g2.setColor(color.brighter());
        g2.drawLine(x + 2, y + 2, x + CELL_SIZE - 3, y + 2);
        g2.drawLine(x + 2, y + 2, x + 2, y + CELL_SIZE - 3);
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(new Color(55, 55, 62));
        for (int x = 0; x <= Board.WIDTH * CELL_SIZE; x += CELL_SIZE) {
            g2.drawLine(x, 0, x, Board.HEIGHT * CELL_SIZE);
        }
        for (int y = 0; y <= Board.HEIGHT * CELL_SIZE; y += CELL_SIZE) {
            g2.drawLine(0, y, Board.WIDTH * CELL_SIZE, y);
        }
    }

    private void drawSidePanel(Graphics2D g2) {
        int x = Board.WIDTH * CELL_SIZE;

        g2.setColor(new Color(34, 34, 40));
        g2.fillRect(x, 0, SIDE_PANEL_WIDTH, getHeight());

        ScoreManager score = engine.getScoreManager();
        g2.setColor(Color.WHITE);
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        g2.drawString("Next", x + 20, 35);
        drawPreview(g2, engine.getNextPiece(), x + 35, 55);

        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        g2.drawString("Score", x + 20, 175);
        g2.drawString(String.valueOf(score.getScore()), x + 20, 198);
        g2.drawString("Lines", x + 20, 235);
        g2.drawString(String.valueOf(score.getLinesCleared()), x + 20, 258);
        g2.drawString("Level", x + 20, 295);
        g2.drawString(String.valueOf(score.getLevel()), x + 20, 318);
        g2.drawString("Music", x + 20, 355);
        g2.drawString(getMusicStatus(), x + 20, 378);

        g2.setColor(new Color(190, 190, 200));
        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        g2.drawString("Arrow keys: move / rotate", x + 12, 430);
        g2.drawString("Space: hard drop", x + 12, 450);
        g2.drawString("P: pause", x + 12, 470);
        g2.drawString("R: restart", x + 12, 490);
        g2.drawString("M: music on/off", x + 12, 510);
    }

    private String getMusicStatus() {
        AudioManager audioManager = engine.getAudioManager();
        if (!audioManager.isMusicAvailable()) {
            return "unavailable";
        }
        return audioManager.isMusicEnabled() ? "on" : "off";
    }

    private void drawPreview(Graphics2D g2, Tetromino piece, int startX, int startY) {
        if (piece == null) {
            return;
        }

        for (int[] block : piece.getBlocks()) {
            int x = startX + block[0] * PREVIEW_CELL_SIZE;
            int y = startY + block[1] * PREVIEW_CELL_SIZE;
            g2.setColor(piece.getColor());
            g2.fillRect(x + 1, y + 1, PREVIEW_CELL_SIZE - 2, PREVIEW_CELL_SIZE - 2);
            g2.setColor(piece.getColor().brighter());
            g2.drawRect(x + 1, y + 1, PREVIEW_CELL_SIZE - 2, PREVIEW_CELL_SIZE - 2);
        }
    }

    private void drawOverlay(Graphics2D g2, String title, String subtitle) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, Board.WIDTH * CELL_SIZE, getHeight());

        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 34));
        FontMetrics metrics = g2.getFontMetrics();
        int x = (Board.WIDTH * CELL_SIZE - metrics.stringWidth(title)) / 2;
        int y = getHeight() / 2 - 25;

        g2.setColor(Color.WHITE);
        g2.drawString(title, x, y);

        g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        metrics = g2.getFontMetrics();
        x = (Board.WIDTH * CELL_SIZE - metrics.stringWidth(subtitle)) / 2;
        g2.drawString(subtitle, x, y + 35);

        if (engine.getState() == GameState.GAME_OVER) {
            String scoreText = "Score: " + engine.getScoreManager().getScore();
            x = (Board.WIDTH * CELL_SIZE - metrics.stringWidth(scoreText)) / 2;
            g2.drawString(scoreText, x, y + 62);
        }
    }
}

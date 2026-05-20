package tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel {
    private static final int CELL_SIZE = 30;
    private static final int TIMER_DELAY = 500;

    private final GameEngine engine = new GameEngine();
    private final Timer timer;

    public GamePanel() {
        setPreferredSize(new Dimension(Board.WIDTH * CELL_SIZE, Board.HEIGHT * CELL_SIZE));
        setBackground(new Color(24, 24, 28));
        setFocusable(true);

        timer = new Timer(TIMER_DELAY, event -> {
            engine.tick();
            repaint();
            if (engine.isGameOver()) {
                ((Timer) event.getSource()).stop();
            }
        });
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (engine.isGameOver()) {
                    return;
                }

                switch (event.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        engine.moveLeft();
                        break;
                    case KeyEvent.VK_RIGHT:
                        engine.moveRight();
                        break;
                    case KeyEvent.VK_DOWN:
                        engine.moveDown();
                        break;
                    case KeyEvent.VK_UP:
                        engine.rotate();
                        break;
                    default:
                        return;
                }
                repaint();
            }
        });
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

        if (engine.isGameOver()) {
            drawGameOver(g2);
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

    private void drawGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 170));
        g2.fillRect(0, 0, getWidth(), getHeight());

        String text = "Game Over";
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 32));
        FontMetrics metrics = g2.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth(text)) / 2;
        int y = (getHeight() + metrics.getAscent()) / 2;

        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);
    }
}

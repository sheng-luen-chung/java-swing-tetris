package tetris;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputHandler extends KeyAdapter {
    private final GameEngine engine;
    private final Runnable afterInput;

    public InputHandler(GameEngine engine, Runnable afterInput) {
        this.engine = engine;
        this.afterInput = afterInput;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                engine.moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                engine.moveRight();
                break;
            case KeyEvent.VK_UP:
                engine.rotate();
                break;
            case KeyEvent.VK_DOWN:
                engine.softDrop();
                break;
            case KeyEvent.VK_SPACE:
                engine.hardDrop();
                break;
            case KeyEvent.VK_P:
                engine.togglePause();
                break;
            case KeyEvent.VK_R:
                engine.restart();
                break;
            default:
                return;
        }

        afterInput.run();
    }
}

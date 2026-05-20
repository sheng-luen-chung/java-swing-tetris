# Swing Event Handling 說明

這個版本使用 Swing 的事件模型來推動遊戲。重點是 `Timer`、鍵盤事件、`repaint()` 與 `paintComponent()` 彼此的分工。

## Timer：遊戲自動更新

`GamePanel` 建立 `javax.swing.Timer`：

```java
timer = new Timer(500, event -> {
    engine.tick();
    repaint();
});
```

Timer 每 500 毫秒觸發一次。每次觸發時：

1. 呼叫 `engine.tick()`，讓目前方塊往下移動。
2. 呼叫 `repaint()`，通知 Swing 重新繪製畫面。
3. 如果遊戲結束，就停止 Timer。

`javax.swing.Timer` 的事件會在 Swing Event Dispatch Thread 執行，因此適合更新 Swing 元件狀態。

## 鍵盤事件：玩家控制

`GamePanel` 使用 `KeyListener` 監聽鍵盤：

```java
addKeyListener(new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent event) {
        // Left, Right, Down, Up
    }
});
```

按鍵對應：

- Left 呼叫 `engine.moveLeft()`
- Right 呼叫 `engine.moveRight()`
- Down 呼叫 `engine.moveDown()`
- Up 呼叫 `engine.rotate()`

每次鍵盤改變遊戲狀態後，也會呼叫 `repaint()`。

## repaint()：要求重畫

`repaint()` 不會立刻直接畫圖。它的意思是告訴 Swing：「這個元件需要重新繪製」。

Swing 會在合適的時間呼叫 `paintComponent(Graphics g)`。

## paintComponent()：真正畫畫的地方

`GamePanel` 覆寫 `paintComponent()`：

```java
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    // draw board, piece, grid, game over
}
```

這裡負責把目前的遊戲狀態畫到畫面上：

1. 畫已固定在 `Board` 裡的方塊。
2. 畫正在下落的 `Tetromino`。
3. 畫格線。
4. 如果 `GameEngine` 回報 Game Over，就畫出 Game Over 文字。

## 整體流程

```text
Timer 觸發
  -> GameEngine 更新方塊位置
  -> repaint()
  -> Swing 呼叫 paintComponent()
  -> 畫面更新

玩家按鍵
  -> KeyListener 收到事件
  -> GameEngine 移動或旋轉方塊
  -> repaint()
  -> Swing 呼叫 paintComponent()
  -> 畫面更新
```

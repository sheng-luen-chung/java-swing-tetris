# Swing Event Handling 說明

Version 2 把 Swing 事件處理分成四個角色：`Timer` 推動時間，`InputHandler` 接收鍵盤，`GameEngine` 更新規則狀態，`GamePanel` 負責畫面。

## Timer：固定時間推進遊戲

`GamePanel` 建立 `javax.swing.Timer`：

```java
timer = new Timer(engine.getDropDelay(), event -> {
    engine.tick();
    ((Timer) event.getSource()).setDelay(engine.getDropDelay());
    repaint();
});
```

每次 Timer 觸發時：

1. 呼叫 `engine.tick()`。
2. 如果狀態是 `RUNNING`，目前方塊往下移動；如果不能下移，就固定、消行、計分並產生下一個方塊。
3. 呼叫 `engine.getDropDelay()` 更新 Timer 延遲，讓等級越高速度越快。
4. 呼叫 `repaint()` 要求 Swing 重畫畫面。

Timer 在 `PAUSED` 或 `GAME_OVER` 時仍可觸發，但 `GameEngine` 會因狀態不是 `RUNNING` 而不更新遊戲規則。這讓玩家仍然可以按 `R` 重新開始。

## InputHandler：集中鍵盤操作

Version 1 的鍵盤事件寫在 `GamePanel` 裡。Version 2 新增 `InputHandler`，讓輸入控制集中管理：

```text
Left  -> engine.moveLeft()
Right -> engine.moveRight()
Up    -> engine.rotate()
Down  -> engine.softDrop()
Space -> engine.hardDrop()
P     -> engine.togglePause()
R     -> engine.restart()
```

`InputHandler` 不直接改畫面，只呼叫 `GameEngine`。操作完成後執行 `afterInput`，由 `GamePanel` 更新 Timer delay 並呼叫 `repaint()`。

## GameEngine：規則與狀態

`GameEngine` 是遊戲規則中心。它知道：

- 目前方塊與下一個方塊。
- 棋盤狀態。
- 分數、消行數、等級。
- 目前是 `RUNNING`、`PAUSED` 或 `GAME_OVER`。

這樣 `GamePanel` 不需要知道如何計分，也不需要知道消行規則。它只讀取狀態並畫出來。

## GamePanel：顯示目前狀態

`GamePanel.paintComponent()` 只做繪圖：

1. 畫棋盤中已固定的方塊。
2. 畫目前正在下落的方塊。
3. 畫格線。
4. 畫右側資訊面板：Next、Score、Lines、Level。
5. 如果狀態是 `PAUSED`，畫暫停 overlay。
6. 如果狀態是 `GAME_OVER`，畫 Game Over overlay、最終分數與重新開始提示。

## repaint() 與 paintComponent()

`repaint()` 不會立刻畫圖。它只是通知 Swing：「這個元件需要重畫」。

Swing 之後會在 Event Dispatch Thread 呼叫：

```java
protected void paintComponent(Graphics g)
```

所有畫面更新都應該集中在 `paintComponent()`，不要在 `Timer` 或 `InputHandler` 裡直接拿 `Graphics` 畫圖。

## 整體流程

```text
Timer 觸發
  -> GameEngine.tick()
  -> GameEngine 根據 GameState 決定是否更新
  -> GamePanel 更新 Timer delay
  -> repaint()
  -> paintComponent()

玩家按鍵
  -> InputHandler.keyPressed()
  -> 呼叫 GameEngine 對應方法
  -> afterInput 更新 Timer delay 並 repaint()
  -> paintComponent()
```
